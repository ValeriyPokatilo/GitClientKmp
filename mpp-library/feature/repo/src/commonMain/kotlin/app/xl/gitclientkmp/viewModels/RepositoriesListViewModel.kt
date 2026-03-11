package app.xl.gitclientkmp.viewModels

import app.xl.gitclientkmp.domain.AppRepository
import app.xl.gitclientkmp.domain.entity.Repository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RepositoriesListViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: Flow<Action> = _actions

    init {
        loadRepositories()
    }

    fun onLogoutButtonPressed() {

    }

    fun onRepositoryItemPressed(repository: Repository) {

    }

    fun onRetryButtonPressed() {

    }

    private fun loadRepositories() {

    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repository>) : State
        data class Error(val error: String) : State
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
