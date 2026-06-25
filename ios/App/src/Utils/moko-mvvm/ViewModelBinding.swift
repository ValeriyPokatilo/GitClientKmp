import Combine
import MultiPlatformLibrary
import SwiftUI

private class BindingData<R> {
    let binding: Binding<R>
    let disposable: DisposableHandle

    init(binding: Binding<R>, disposable: DisposableHandle) {
        self.binding = binding
        self.disposable = disposable
    }

    deinit {
        disposable.dispose()
    }
}

public extension ObservableObject where Self: ViewModel {
    // TODO: поправить реализацию с кэшированием в AssociatedObject
    // На экране с выбором даты и времени не обновлялось вью при изменении значения StateFlow (см. GGC-507)
    // Дебаг привел к тому, что у создаваемого биндинга не вызывается getter, возможно отсутствует отслеживание stateFlow как источника данных

    /*
     private func bindingCore<T, R>(
         _ flowKey: KeyPath<Self, CMutableStateFlow<T>>,
         equals: @escaping (T?, T?) -> Bool,
         getMapper: @escaping (T?) -> R,
         setMapper: @escaping (R) -> T?
     ) -> Binding<R> {
         let stateFlow: CMutableStateFlow<T> = self[keyPath: flowKey]

         let associatedKey = Unmanaged.passUnretained(stateFlow).toOpaque()

         let bindingData: BindingData<R>? = objc_getAssociatedObject(
             self,
             associatedKey
         ) as? BindingData<R>

         // у нас уже есть биндинг ранее созданный, нам не надо по новой его
         // создавать и делать новую подписку на flow
         if let data = bindingData {
             return data.binding
         }

         // нет биндинга, надо создать новый и сохранить
         var lastValue: T? = stateFlow.value
         let disposable = stateFlow.subscribe(
             onCollect: { [weak self] value in
                 if !equals(lastValue, value) {
                     lastValue = value
                     self?.objectWillChange.send()
                 }
             }
         )

         let data = BindingData<R>(
             binding: Binding<R>(
                 get: {
                     let val = getMapper(stateFlow.value)
                     return val
                 },
                 set: {
                     stateFlow.value = setMapper($0)
                 }
             ),
             disposable: disposable
         )

         objc_setAssociatedObject(
             self,
             associatedKey,
             data,
             .OBJC_ASSOCIATION_RETAIN
         )

         return data.binding
     }*/

    private func bindingCore<T, R>(
        _ flowKey: KeyPath<Self, CMutableStateFlow<T>>,
        equals: @escaping (T?, T?) -> Bool,
        getMapper: @escaping (T?) -> R,
        setMapper: @escaping (R) -> T?
    ) -> Binding<R> {
        let stateFlow: CMutableStateFlow<T> = self[keyPath: flowKey]
        var lastValue: T? = stateFlow.value

        var disposable: DisposableHandle?

        disposable = stateFlow.subscribe { [weak self] value in
            if !equals(lastValue, value) {
                lastValue = value
                self?.objectWillChange.send()
                disposable?.dispose()
            }
        }

        return Binding(
            get: {
                getMapper(stateFlow.value)
            },
            set: {
                stateFlow.value = setMapper($0)
            }
        )
    }

    func binding<T, R>(
        _ flowKey: KeyPath<Self, CMutableStateFlow<T>>,
        equals: @escaping (T?, T?) -> Bool,
        getMapper: @escaping (T) -> R,
        setMapper: @escaping (R) -> T
    ) -> Binding<R> {
        return bindingCore(
            flowKey,
            equals: equals,
            getMapper: { getMapper($0!) },
            setMapper: setMapper
        )
    }

    func bindingNullable<T, R>(
        _ flowKey: KeyPath<Self, CMutableStateFlow<T>>,
        equals: @escaping (T?, T?) -> Bool,
        getMapper: @escaping (T?) -> R?,
        setMapper: @escaping (R?) -> T?
    ) -> Binding<R?> {
        return bindingCore(
            flowKey,
            equals: equals,
            getMapper: getMapper,
            setMapper: setMapper
        )
    }

    func binding(_ flowKey: KeyPath<Self, CMutableStateFlow<NSString>>) -> Binding<String> {
        return binding(
            flowKey,
            equals: { $0 == $1 },
            getMapper: { $0 as String },
            setMapper: { $0 as NSString }
        )
    }

    func binding(_ flowKey: KeyPath<Self, CMutableStateFlow<KotlinBoolean>>) -> Binding<Bool> {
        return binding(
            flowKey,
            equals: { $0?.boolValue == $1?.boolValue },
            getMapper: { $0.boolValue },
            setMapper: { KotlinBoolean(bool: $0) }
        )
    }

    func binding(_ flowKey: KeyPath<Self, CMutableStateFlow<KotlinInt>>) -> Binding<Int> {
        return binding(
            flowKey,
            equals: { $0?.intValue == $1?.intValue },
            getMapper: { $0.intValue },
            setMapper: { KotlinInt(int: Int32($0)) }
        )
    }

    func binding(_ flowKey: KeyPath<Self, CMutableStateFlow<KotlinLong>>) -> Binding<Int64> {
        return binding(
            flowKey,
            equals: { $0?.int64Value == $1?.int64Value },
            getMapper: { $0.int64Value },
            setMapper: { KotlinLong(longLong: $0) }
        )
    }

    func binding(_ flowKey: KeyPath<Self, CMutableStateFlow<KotlinFloat>>) -> Binding<Float> {
        return binding(
            flowKey,
            equals: { $0?.floatValue == $1?.floatValue },
            getMapper: { $0.floatValue },
            setMapper: { KotlinFloat(float: $0) }
        )
    }

    func binding(_ flowKey: KeyPath<Self, CMutableStateFlow<KotlinDouble>>) -> Binding<Double> {
        return binding(
            flowKey,
            equals: { $0?.doubleValue == $1?.doubleValue },
            getMapper: { $0.doubleValue },
            setMapper: { KotlinDouble(double: $0) }
        )
    }

    func bindingNullable(_ flowKey: KeyPath<Self, CMutableStateFlow<Kotlinx_datetimeLocalDate>>) -> Binding<Date?> {
        return bindingNullable(
            flowKey,
            equals: { $0?.toSwiftDate() == $1?.toSwiftDate() },
            getMapper: { $0?.toSwiftDate() },
            setMapper: { $0?.toLocalDate() }
        )
    }

    func bindingNullable(_ flowKey: KeyPath<Self, CMutableStateFlow<Kotlinx_datetimeLocalTime>>) -> Binding<Date?> {
        return bindingNullable(
            flowKey,
            equals: { $0?.toSwiftDate() == $1?.toSwiftDate() },
            getMapper: { $0?.toSwiftDate() },
            setMapper: { $0?.toLocalTime() }
        )
    }

    func bindingWithDefault(
        _ flowKey: KeyPath<Self, CMutableStateFlow<Kotlinx_datetimeLocalDate>>,
        defaultValue: Date
    ) -> Binding<Date> {
        return bindingCore(
            flowKey,
            equals: { $0?.toSwiftDate() == $1?.toSwiftDate() },
            getMapper: { $0?.toSwiftDate() ?? defaultValue },
            setMapper: { $0.toLocalDate() }
        )
    }

    func bindingWithDefault(
        _ flowKey: KeyPath<Self, CMutableStateFlow<Kotlinx_datetimeLocalTime>>,
        defaultValue: Date
    ) -> Binding<Date> {
        return bindingCore(
            flowKey,
            equals: { $0?.toSwiftDate() == $1?.toSwiftDate() },
            getMapper: { $0?.toSwiftDate() ?? defaultValue },
            setMapper: { $0.toLocalTime() }
        )
    }
}
