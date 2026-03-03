//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Combine
import Foundation
import MultiPlatformLibrary

private var cFlowPublisherKey = UUID().uuidString

public func createPublisher<T>(_ cFlow: CFlow<T>, debug: String? = nil) -> AnyPublisher<T, Never> {
    // чтобы небыло постоянных переподписок на поток, из-за которых
    // мы можем пропустить какие либо события, мы сохраняем созданный паблишер
    // и храним его в связке с CFlow. Пока CFlow жив - жив и Publisher
    // а это позволяет не переподписываться постоянно вьюхам между разными
    // паблишерами
    let associatedKey = UnsafeRawPointer(Unmanaged.passUnretained(cFlowPublisherKey as NSString).toOpaque())

    let existObject: Any? = objc_getAssociatedObject(
        cFlow,
        associatedKey
    )

    if let existObject {
        return existObject as! AnyPublisher<T, Never>
    }

    let newObject: AnyPublisher<T, Never> = CFlowPublisher(
        cFlow: cFlow,
        debug: debug
    ).eraseToAnyPublisher()

    objc_setAssociatedObject(
        cFlow,
        associatedKey,
        newObject,
        .OBJC_ASSOCIATION_RETAIN
    )

    return newObject
}

private struct CFlowPublisher<Output: AnyObject>: Publisher {
    typealias Output = Output
    typealias Failure = Never

    let cFlow: CFlow<Output>
    let debug: String?

    func receive<S>(subscriber: S) where S: Subscriber, Failure == S.Failure, Output == S.Input {
        subscriber.receive(
            subscription: CFlowSubscription(
                flow: cFlow,
                debug: debug,
                subscriber: subscriber
            )
        )
    }
}

private class CFlowSubscription<Output: AnyObject, S: Subscriber>: Subscription where S.Input == Output, S.Failure == Never {
    private let disposable: DisposableHandle
    private let debug: String?
    private let subscriber: S

    init(flow: CFlow<Output>, debug: String?, subscriber: S) {
        self.subscriber = subscriber
        self.debug = debug
        // TRICKY: `as! CFlow<AnyObject>` cast here, and `as! Output` cast below, combine
        // to work around https://github.com/apple/swift/issues/65331
        self.disposable = (flow as! CFlow<AnyObject>).subscribe { value in
            _ = subscriber.receive(value as! Output)
        }
    }

    func request(_: Subscribers.Demand) {}

    func cancel() {
        self.disposable.dispose()
    }
}
