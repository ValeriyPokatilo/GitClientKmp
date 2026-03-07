package app.xl.gitclientkmp

import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private var token = ""

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.cStateFlow()

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
                // TODO: - repository.signIn(token)
                _state.value = State.Idle
                _actions.emit(Action.RouteToMain)
            } catch (error: Exception) {
                _state.value = State.Idle
                // TODO: - handle error
            }
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        object InvalidInput : State
    }

    sealed interface Action {
        data class ShowError(val code: Int?, val message: String?) : Action
        object RouteToMain : Action
        object FocusOnTokenField : Action
    }
}
