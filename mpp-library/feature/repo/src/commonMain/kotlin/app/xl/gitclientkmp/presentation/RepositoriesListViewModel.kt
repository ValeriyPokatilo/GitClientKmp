package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.data.utils.ColorProvider
import app.xl.gitclientkmp.domain.entity.Repository
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.repository.AppRepository
import dev.icerock.moko.errors.mappers.mapThrowable
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepositoriesListViewModel(
    private val repository: AppRepository,
    private val colorProvider: ColorProvider
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    fun onStart() {
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
                val repositories: List<Repository> = repository.getRepositories()

                if (repositories.isEmpty()) {
                    _state.value = State.Empty
                } else {
                    val repositoriesWithColors: List<Repository> = addLanguageColors(repositories)
                    _state.value = State.Loaded(repositoriesWithColors)
                }
            } catch (error: Exception) {
                val errorModel: ErrorModel = error.mapThrowable()
                _state.value = State.Error(errorModel)
            }
        }
    }

    private fun addLanguageColors(repositories: List<Repository>): List<Repository> {
        val languages: Set<String> = repositories.mapNotNull { it.language }.toSet()
        val languageColorMap: Map<String, Int> = languages.associateWith {
            colorProvider.getColor(it)
        }

        return repositories.map { repo ->
            repo.copy(languageColor = repo.language?.let { languageColorMap[it] })
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repositories: List<Repository>) : State
        data class Error(val error: ErrorModel) : State
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
