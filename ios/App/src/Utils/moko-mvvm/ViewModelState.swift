//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Combine
import MultiPlatformLibrary
import SwiftUI

public extension ObservableObject where Self: ViewModel {
    func state<T, R>(
        stateFlow: CStateFlow<T>,
        equals: @escaping (T, T) -> Bool,
        mapper: @escaping (T) -> R
    ) -> R {
        var lastValue: T = stateFlow.value!

        var disposable: DisposableHandle?

        disposable = stateFlow.subscribe(onCollect: { [weak self] value in
            if !equals(lastValue, value!) {
                lastValue = value!
                self?.objectWillChange.send()
                disposable?.dispose()
                disposable = nil
            }
        })

        return mapper(stateFlow.value!)
    }

    func state<T, R>(
        _ flowKey: KeyPath<Self, CStateFlow<T>>,
        equals: @escaping (T, T) -> Bool,
        mapper: @escaping (T) -> R
    ) -> R {
        let stateFlow: CStateFlow<T> = self[keyPath: flowKey]
        return state(stateFlow: stateFlow, equals: equals, mapper: mapper)
    }

    func state<T, R>(
        _ flowKey: KeyPath<Self, CMutableStateFlow<T>>,
        equals: @escaping (T, T) -> Bool,
        mapper: @escaping (T) -> R
    ) -> R {
        let stateFlow: CMutableStateFlow<T> = self[keyPath: flowKey]
        return state(stateFlow: stateFlow, equals: equals, mapper: mapper)
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<KotlinBoolean>>) -> Bool {
        return state(
            flowKey,
            equals: { $0.boolValue == $1.boolValue },
            mapper: { $0.boolValue }
        )
    }

    func state(_ flowKey: KeyPath<Self, CMutableStateFlow<KotlinBoolean>>) -> Bool {
        return state(
            flowKey,
            equals: { $0.boolValue == $1.boolValue },
            mapper: { $0.boolValue }
        )
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<KotlinDouble>>) -> Double {
        return state(
            flowKey,
            equals: { $0.doubleValue == $1.doubleValue },
            mapper: { $0.doubleValue }
        )
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<KotlinFloat>>) -> Float {
        return state(
            flowKey,
            equals: { $0.floatValue == $1.floatValue },
            mapper: { $0.floatValue }
        )
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<KotlinInt>>) -> Int {
        return state(
            flowKey,
            equals: { $0.intValue == $1.intValue },
            mapper: { $0.intValue }
        )
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<KotlinLong>>) -> Int64 {
        return state(
            flowKey,
            equals: { $0.int64Value == $1.int64Value },
            mapper: { $0.int64Value }
        )
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<NSString>>) -> String {
        return state(
            flowKey,
            equals: { $0 == $1 },
            mapper: { $0 as String }
        )
    }

    func state(_ flowKey: KeyPath<Self, CStateFlow<StringDesc>>) -> String {
        return state(
            flowKey,
            equals: { $0 === $1 },
            mapper: { $0.localized() }
        )
    }

    func state<T>(_ flowKey: KeyPath<Self, CStateFlow<NSArray>>) -> [T] {
        return state(
            flowKey,
            equals: { oldValue, newValue in
                guard let newValue = newValue as? [T] else {
                    return false
                }
                return oldValue.isEqual(to: newValue)
            },
            mapper: { $0 as! [T] }
        )
    }

    func state<T: KotlinBase>(_ flowKey: KeyPath<Self, CStateFlow<T>>) -> T {
        return state(
            flowKey,
            equals: { $0.isEqual($1) },
            mapper: { $0 }
        )
    }

    func state<T: KotlinBase>(_ flowKey: KeyPath<Self, CMutableStateFlow<T>>) -> T {
        return state(
            flowKey,
            equals: { $0.isEqual($1) },
            mapper: { $0 }
        )
    }
}
