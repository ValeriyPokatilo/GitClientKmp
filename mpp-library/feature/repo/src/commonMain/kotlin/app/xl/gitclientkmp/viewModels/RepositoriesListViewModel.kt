package app.xl.gitclientkmp.viewModels

import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.Repository
import app.xl.gitclientkmp.domain.repository.AppRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepositoriesListViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _action = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    init {
        loadRepositories()
    }

    fun onLogoutButtonPressed() {
        viewModelScope.launch {
            repository.logout()
            _action.emit(Action.Logout)
        }
    }

    fun onRepositoryItemPressed(repository: Repository) {
        viewModelScope.launch {
            _action.emit(
                Action.RouteToDetail(
                    owner = repository.owner.login,
                    repositoryName = repository.name,
                    branch = repository.defaultBranch
                )
            )
        }
    }

    fun onRetryButtonPressed() {
        loadRepositories()
    }

    private fun loadRepositories() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val repositories = repository.getRepositories()

                if (repositories.isEmpty()) {
                    _state.value = State.Empty
                } else {
                    _state.value = State.Loaded(repositories)
                }
            } catch (error: AppError) {
                _state.value = State.Error(error)
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repositories: List<Repository>) : State
        data class Error(val error: AppError) : State
        object Empty : State
    }

    sealed interface Action {
        object Logout : Action
        data class RouteToDetail(
            val owner: String,
            val repositoryName: String,
            val branch: String
        ) : Action
    }
}
