package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.Issue
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

class IssueInfoViewModel(
    private val repository: AppRepository,
    private val owner: String,
    private val repositoryName: String,
    private val issueNumber: Int
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    private var isLoaded = false

    fun onStart() {
        if (isLoaded) return
        isLoaded = true
        loadIssueInfo()
    }

    fun loadIssueInfo() {
        Logger.info(message = "IssueInfoViewModel: loadIssueInfo")
        viewModelScope.launch {
            Logger.info(message = "IssueInfoViewModel: State - Loading")
            _state.value = State.Loading
            try {
                val issue: Issue = repository.getIssue(
                    ownerName = owner,
                    repositoryName = repositoryName,
                    issueNumber = issueNumber
                )

                Logger.info(message = "IssueInfoViewModel: State - Loaded")
                _state.value = State.Loaded(
                    issue = issue
                )
            } catch (error: Exception) {
                Logger.info(message = "IssueInfoViewModel: State - Error")
                val errorModel: ErrorModel = error.mapThrowable()
                _state.value = State.Error(error = errorModel)
            }
        }
    }

    fun onBackButtonPressed() {
        Logger.info(message = "IssueInfoViewModel: onBackButtonPressed")
        viewModelScope.launch {
            Logger.info(message = "IssueInfoViewModel: Action - RouteToBack")
            _action.emit(value = Action.RouteToBack)
        }
    }

    fun onRetryButtonPressed() {
        Logger.info(message = "IssueInfoViewModel: onRetryButtonPressed")
        loadIssueInfo()
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val issue: Issue) : State
        data class Error(val error: ErrorModel) : State
    }

    sealed interface Action {
        object RouteToBack : Action
    }
}
