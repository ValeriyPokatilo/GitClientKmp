package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.repository.AppRepository
import dev.icerock.moko.errors.mappers.mapThrowable
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private var token: String = ""

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    private val _actions: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _actions

    private val githubTokenInputRegex: Regex = Regex("^g?$|^gh?$|^ghp?$|^ghp_?[a-zA-Z0-9]*$")
    private val githubTokenValidateRegex: Regex = Regex("^ghp_[a-zA-Z0-9]{36}$")

    fun onTokenChanged(text: String) {
        token = text
        _state.value = when {
            !githubTokenInputRegex.matches(text) -> State.InvalidInput
            else -> State.Idle
        }
    }

    fun onSignButtonPressed() {
        if (token.isBlank()) {
            viewModelScope.launch {
                _actions.emit(Action.FocusOnTokenField)
            }
            return
        }

        if (!githubTokenValidateRegex.matches(token)) {
            _state.value = State.InvalidInput
            return
        }

        login()
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                repository.signIn(token)
                _actions.emit(Action.RouteToMain)
            } catch (error: Exception) {
                _state.value = State.Idle
                handleError(error)
            }
        }
    }

    private suspend fun handleError(error: Exception) {
        val errorModel: ErrorModel = error.mapThrowable()

        _actions.emit(
            Action.ShowError(errorModel)
        )
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        object InvalidInput : State
    }

    sealed interface Action {
        data class ShowError(val error: ErrorModel) : Action
        object RouteToMain : Action
        object FocusOnTokenField : Action
    }
}
