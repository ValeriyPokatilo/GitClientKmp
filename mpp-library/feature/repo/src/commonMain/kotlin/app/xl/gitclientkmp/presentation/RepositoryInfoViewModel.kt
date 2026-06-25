package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.RepositoryDetails
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

class RepositoryInfoViewModel(
    private val repository: AppRepository,
    private val owner: String,
    private val repositoryName: String,
    private val branch: String
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    private var isLoaded = false

    fun onStart() {
        if (isLoaded) return
        isLoaded = true
        loadRepositoryInfo()
    }

    fun onBackButtonPressed() {
        Logger.info(message = "RepositoryInfoViewModel: onBackButtonPressed")
        viewModelScope.launch {
            _action.emit(value = Action.RouteBack)
        }
    }

    fun onLogoutPressed() {
        Logger.info(message = "RepositoryInfoViewModel: onLogoutPressed")
        viewModelScope.launch {
            repository.logout()
            Logger.info(message = "RepositoryInfoViewModel: Action - Logout")
            _action.emit(value = Action.Logout)
        }
    }

    fun onRetryButtonPressed() {
        Logger.info(message = "RepositoryInfoViewModel: onRetryButtonPressed")
        loadRepositoryInfo()
    }

    private fun loadRepositoryInfo() {
        viewModelScope.launch {
            Logger.info(message = "RepositoryInfoViewModel: State - Loading")
            _state.value = State.Loading
            try {
                val details = repository.getRepository(
                    ownerName = owner,
                    repositoryName = repositoryName
                )

                _state.value = State.Loaded(
                    githubRepo = details,
                    readmeState = ReadmeState.Loading
                )

                loadReadme()
            } catch (error: Exception) {
                Logger.info(message = "RepositoryInfoViewModel: State - Error")
                val errorModel: ErrorModel = error.mapThrowable()
                _state.value = State.Error(error = errorModel)
            }
        }
    }

    private suspend fun loadReadme() {
        try {
            val readme: String = repository.getRepositoryReadme(
                ownerName = owner,
                repositoryName = repositoryName,
                branchName = branch
            )

            val readmeState: ReadmeState = if (readme.isBlank()) {
                Logger.info(message = "RepositoryInfoViewModel: ReadmeState - Empty")
                ReadmeState.Empty
            } else {
                Logger.info(message = "RepositoryInfoViewModel: ReadmeState - Loaded")
                ReadmeState.Loaded(markdown = readme)
            }

            updateReadmeState(readmeState)
        } catch (error: Exception) {
            Logger.info(message = "RepositoryInfoViewModel: ReadmeState - Error")
            val errorModel: ErrorModel = error.mapThrowable()
            updateReadmeState(ReadmeState.Error(error = errorModel))
        }
    }

    private fun updateReadmeState(readmeState: ReadmeState) {
        val current: State = _state.value
        if (current is State.Loaded) {
            _state.value = current.copy(readmeState = readmeState)
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: ErrorModel) : State

        data class Loaded(
            val githubRepo: RepositoryDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: ErrorModel) : ReadmeState
        data class Loaded(val markdown: String?) : ReadmeState
    }

    sealed interface Action {
        object Logout : Action
        object RouteBack : Action
    }
}
