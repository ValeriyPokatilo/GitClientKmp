package app.xl.gitclientkmp.viewModels

import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.RepositoryDetails
import app.xl.gitclientkmp.domain.repository.AppRepository
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

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _action = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    init {
        loadRepositoryInfo()
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            _action.emit(Action.RouteBack)
        }
    }

    fun onLogoutPressed() {
        viewModelScope.launch {
            repository.logout()
            _action.emit(Action.Logout)
        }
    }

    fun onRetryButtonPressed() {
        loadRepositoryInfo()
    }

    private fun loadRepositoryInfo() {
        viewModelScope.launch {
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
            } catch (error: AppError) {
                _state.value = State.Error(error)
            }
        }
    }

    private suspend fun loadReadme() {
        try {
            val readme = repository.getRepositoryReadme(
                ownerName = owner,
                repositoryName = repositoryName,
                branchName = branch
            )

            val readmeState = if (readme.isBlank()) {
                ReadmeState.Empty
            } else {
                ReadmeState.Loaded(readme)
            }

            updateReadmeState(readmeState)
        } catch (error: AppError) {
            updateReadmeState(ReadmeState.Error(error))
        }
    }

    private fun updateReadmeState(readmeState: ReadmeState) {
        val current = _state.value
        if (current is State.Loaded) {
            _state.value = current.copy(readmeState = readmeState)
        }
    }

    sealed interface State {
        object Loading : State
        data class Error(val error: AppError) : State

        data class Loaded(
            val githubRepo: RepositoryDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: AppError) : ReadmeState
        data class Loaded(val markdown: String?) : ReadmeState
    }

    sealed interface Action {
        object Logout : Action
        object RouteBack : Action
    }
}
