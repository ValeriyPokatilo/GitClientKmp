package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.Issue
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.repository.AppRepository
import app.xl.gitclientkmp.logger.Logger
import dev.icerock.moko.errors.mappers.mapThrowable
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.dataTransform
import dev.icerock.moko.mvvm.livedata.errorTransform
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mediatorOf
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.paging.LambdaPagedListDataSource
import dev.icerock.moko.paging.Pagination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class IssuesListViewModel(
    private val repository: AppRepository,
    private val owner: String,
    private val repositoryName: String
) : ViewModel() {

    private val issuesPerPage: Int = 20

    private val pagination: Pagination<Issue> = Pagination(
        parentScope = viewModelScope,
        dataSource = LambdaPagedListDataSource { currentList ->
            val pageSize: Int = issuesPerPage
            val page: Int = (currentList?.size ?: 0) / pageSize + 1

            repository.getIssues(
                ownerName = owner,
                repositoryName = repositoryName,
                pageSize = pageSize,
                page = page
            )
        },
        comparator = { a, b -> a.id.compareTo(b.id) },
        nextPageListener = {},
        refreshListener = {},
        initValue = emptyList()
    )

    val state: LiveData<ResourceState<List<UiItem>, ErrorModel>> =
        pagination.state
            .dataTransform {
                mediatorOf(
                    source1 = this,
                    source2 = pagination.nextPageLoading
                ) { list, isNextLoading ->

                    val units: List<UiItem.IssueItem> = list.map { issue ->
                        UiItem.IssueItem(issue = issue)
                    }

                    val shouldShowLoader: Boolean =
                        isNextLoading && list.size >= issuesPerPage

                    if (shouldShowLoader) {
                        units + UiItem.LoaderItem
                    } else {
                        units
                    }
                }
            }
            .errorTransform {
                map { it.mapThrowable() }
            }

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    private var isLoaded = false

    fun onStart() {
        if (isLoaded) return
        isLoaded = true
        pagination.loadFirstPage()
    }

    fun onBackButtonPressed() {
        Logger.info(message = "IssuesListViewModel: onBackButtonPressed")
        viewModelScope.launch {
            _action.emit(value = Action.RouteToBack)
        }
    }

    fun onCreateIssuePressed() {
        Logger.info(message = "IssuesListViewModel: onCreateIssuePressed")
        viewModelScope.launch {
            Logger.info(message = "IssuesListViewModel: Action - RouteToCreate")
            _action.emit(
                value = Action.RouteToCreate(
                    owner = owner,
                    repositoryName = repositoryName
                )
            )
        }
    }

    fun onRetryButtonPressed() {
        Logger.info(message = "IssuesListViewModel: onRetryButtonPressed")
        pagination.loadFirstPage()
    }

    fun onIssueItemPressed(issue: Issue) {
        Logger.info(message = "IssuesListViewModel: onIssueItemPressed - ${issue.title}")
        viewModelScope.launch {
            Logger.info(message = "IssuesListViewModel: Action - RouteToDetail")
            _action.emit(
                value = Action.RouteToDetail(
                    owner = owner,
                    repositoryName = repositoryName,
                    issueNumber = issue.number
                )
            )
        }
    }

    fun onReachEnd() {
        if (pagination.nextPageLoading.value) return
        pagination.loadNextPage()
    }

    sealed interface Action {
        object RouteToBack : Action
        data class RouteToCreate(
            val owner: String,
            val repositoryName: String
        ) : Action
        data class RouteToDetail(
            val owner: String,
            val repositoryName: String,
            val issueNumber: Int
        ) : Action
    }

    sealed interface UiItem {
        data class IssueItem(val issue: Issue) : UiItem
        object LoaderItem : UiItem
    }
}
