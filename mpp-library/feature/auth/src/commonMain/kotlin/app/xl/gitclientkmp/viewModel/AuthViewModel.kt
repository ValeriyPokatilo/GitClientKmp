package app.xl.gitclientkmp.viewModel

import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.repository.AppRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private var token = ""

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val action: Flow<Action> = _actions

    private val githubTokenRegex = Regex("^[A-Za-z0-9_-]*$")

    fun onTokenChanged(text: String) {
        token = text
        _state.value = when {
            !githubTokenRegex.matches(text) -> State.InvalidInput
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

        viewModelScope.launch {
            _state.value = State.Loading
            try {
                repository.signIn(token)
                _state.value = State.Idle
                _actions.emit(Action.RouteToMain)
            } catch (exc: AppError) {
                _state.value = State.Idle
                handleError(exc)
            } catch (exc: Exception) {
                _state.value = State.Idle
                handleError(AppError.Network(exc))
            }
        }
    }

    private suspend fun handleError(error: AppError) {
        val message = when (error) {
            is AppError.Http -> "${error.errorMessage} / ${error.code}"
            is AppError.Network -> null
        }

        _actions.emit(
            Action.ShowError(message)
        )
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        object InvalidInput : State
    }

    sealed interface Action {
        data class ShowError(val message: String?) : Action
        object RouteToMain : Action
        object FocusOnTokenField : Action
    }
}
