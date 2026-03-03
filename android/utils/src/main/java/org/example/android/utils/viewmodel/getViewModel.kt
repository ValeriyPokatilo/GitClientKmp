package org.example.android.utils.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import dev.icerock.moko.mvvm.getViewModel
import org.koin.compose.getKoin
import org.koin.core.Koin
import kotlin.reflect.KClass

/**
 * Получить ViewModel из ViewModelStore с созданием нового объекта ViewModel с помощью Koin.
 *
 * Пример:
 * ```
 * val viewModel: SplashViewModel = getViewModel { getSplashViewModel() }
 * ```
 *
 * @param factory лямбда-фабрика для создания нового объекта ViewModel, в ресивер передается Koin
 *
 * @return возвращается либо уже имеющаяся ViewModel из ViewModelStore, либо создается новая и
 * сохраняется в ViewModelStore
 */
@Composable
inline fun <reified T : ViewModel> getViewModel(noinline factory: Koin.() -> T): T {
    return getViewModel(T::class, factory)
}

@Composable
fun <T : ViewModel> getViewModel(klass: KClass<T>, factory: Koin.() -> T): T {
    val koin: Koin = getKoin()
    val viewModelStoreOwner: ViewModelStoreOwner =
        requireNotNull(LocalViewModelStoreOwner.current) {
            "viewModel can be resolved only from LocalViewModelStoreOwner"
        }
    return viewModelStoreOwner.getViewModel(klass) {
        koin.factory()
    }
}
