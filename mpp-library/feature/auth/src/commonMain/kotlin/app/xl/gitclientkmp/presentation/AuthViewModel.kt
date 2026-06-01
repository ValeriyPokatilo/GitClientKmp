package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.repository.AppRepository
import app.xl.gitclientkmp.logger.Logger
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

    private val _state = MutableStateFlow<State>(value = State.Idle)
    val state: StateFlow<State> = _state

    private val _actions: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _actions

    private val githubTokenInputRegex: Regex = Regex("^g?$|^gh?$|^ghp?$|^ghp_?[a-zA-Z0-9]*$")
    private val githubTokenValidateRegex: Regex = Regex("^ghp_[a-zA-Z0-9]{36}$")

    fun onTokenChanged(text: String) {
        Logger.info(message = "AuthViewModel: onTokenChanged $text")
        token = text
        _state.value = when {
            !githubTokenInputRegex.matches(input = text) -> State.InvalidInput
            else -> State.Idle
        }
    }

    fun onSignButtonPressed() {
        Logger.info(message = "AuthViewModel: onSignButtonPressed")
        if (token.isBlank()) {
            viewModelScope.launch {
                Logger.info(message = "AuthViewModel: Action - FocusOnTokenField")
                _actions.emit(value = Action.FocusOnTokenField)
            }
            return
        }

        if (!githubTokenValidateRegex.matches(input = token)) {
            Logger.info(message = "AuthViewModel: State - InvalidInput")
            _state.value = State.InvalidInput
            return
        }

        login()
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                repository.signIn(token = token)
                Logger.info(message = "AuthViewModel: Action - RouteToMain")
                _actions.emit(value = Action.RouteToMain)
            } catch (error: Exception) {
                Logger.info(message = "AuthViewModel: State - Idle")
                _state.value = State.Idle
                handleError(error = error)
            }
        }
    }

    private suspend fun handleError(error: Exception) {
        val errorModel: ErrorModel = error.mapThrowable()

        Logger.info(message = "AuthViewModel: Action - ShowError")
        _actions.emit(
            Action.ShowError(error = errorModel)
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
