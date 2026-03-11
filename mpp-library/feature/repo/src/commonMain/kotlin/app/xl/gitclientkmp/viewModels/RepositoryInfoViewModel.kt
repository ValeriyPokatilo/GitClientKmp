package app.xl.gitclientkmp.viewModels

import app.xl.gitclientkmp.domain.AppRepository
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.RepositoryDetails
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RepositoryInfoViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _actions = MutableSharedFlow<RepositoriesListViewModel.Action>()
    val actions: Flow<RepositoriesListViewModel.Action> = _actions

    init {
        loadRepositoryInfo()
    }

    fun onLogoutPressed() {

    }

    fun onRetryButtonPressed() {

    }

    private fun loadRepositoryInfo() {

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
