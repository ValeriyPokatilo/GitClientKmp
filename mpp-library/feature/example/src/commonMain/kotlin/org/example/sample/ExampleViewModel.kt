package org.example.sample

import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.example.sample.model.ExampleRepository

class ExampleViewModel(
    private val params: Params,
    private val repository: ExampleRepository,
) : ViewModel() {
    private val _actions: Channel<Actions> = Channel()
    val actions: CFlow<Actions> = _actions.receiveAsFlow().cFlow()

    val language: CMutableStateFlow<String> = MutableStateFlow(value = "")
        .cMutableStateFlow()

    fun getArgValue(): String {
        return params.argument
    }

    fun saveAction() {
        repository.saveInKeyValueStorage(value = language.value)
    }

    data class Params(
        val argument: String,
    )

    sealed interface Actions {
        data object SameAction : Actions
    }
}
