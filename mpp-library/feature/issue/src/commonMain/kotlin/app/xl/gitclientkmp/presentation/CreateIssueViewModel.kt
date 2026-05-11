package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.repository.AppRepository
import app.xl.gitclientkmp.logger.Logger
import dev.icerock.moko.errors.mappers.mapThrowable
import dev.icerock.moko.fields.core.validate
import dev.icerock.moko.fields.core.validations.notBlank
import dev.icerock.moko.fields.livedata.FormField
import dev.icerock.moko.fields.livedata.validations.fieldValidation
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateIssueViewModel(
    private val repository: AppRepository,
    private val owner: String,
    private val repositoryName: String
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow<Action>()
    val action: Flow<Action> = _action

    val title: FormField<String, StringDesc> = FormField(
        initialValue = "",
        validation = fieldValidation {
            notBlank(errorText = MR.strings.invalid_title.desc())
        }
    )

    val body: FormField<String, StringDesc> = FormField(
        initialValue = "",
        validation = fieldValidation {
            notBlank(errorText = MR.strings.invalid_description.desc())
        }
    )

    private fun createIssue() {
        Logger.info(message = "CreateIssueViewModel: createIssue")
        viewModelScope.launch {
            Logger.info(message = "CreateIssueViewModel: isLoading = true")
            _state.value = _state.value.copy(isLoading = true)

            try {
                repository.createIssue(
                    ownerName = owner,
                    repositoryName = repositoryName,
                    title = title.value().trim(),
                    body = body.value().trim()
                )
                Logger.info(message = "CreateIssueViewModel: createIssue success")

                Logger.info(message = "CreateIssueViewModel: Action - RouteBack")
                _action.emit(value = Action.RouteBack)
            } catch (error: Exception) {
                Logger.info(message = "CreateIssueViewModel: createIssue failed ${error.message}")
                Logger.info(message = "CreateIssueViewModel: isLoading = false")
                _state.value = _state.value.copy(isLoading = false)
                handleError(error = error)
            }
        }
    }

    private suspend fun handleError(error: Exception) {
        val errorModel: ErrorModel = error.mapThrowable()
        _action.emit(value = Action.ShowError(error = errorModel))
    }

    fun onSubmitButtonPressed() {
        Logger.info(message = "CreateIssueViewModel: onSubmitButtonPressed")
        if (listOf(title, body).validate().not()) {
            return
        }

        createIssue()
    }

    fun onBackButtonPressed() {
        Logger.info(message = "CreateIssueViewModel: onBackButtonPressed")
        viewModelScope.launch {
            _action.emit(value = Action.RouteBack)
        }
    }

    fun onAttachPressed() {
        Logger.info(message = "CreateIssueViewModel: onAttachPressed")
        viewModelScope.launch {
            Logger.info(message = "CreateIssueViewModel: Action - OpenImagePicker")
            _action.emit(value = Action.OpenImagePicker)
        }
    }

    fun onArrowPressed() {
        Logger.info(message = "CreateIssueViewModel: onArrowPressed")
        _state.value = _state.value.copy(
            isExpanded = !_state.value.isExpanded
        )
    }

    fun onFilesSelected(files: List<ByteArray>) {
        Logger.info(message = "CreateIssueViewModel: onFilesSelected")
        _state.value = _state.value.copy(
            isExpanded = true,
            totalCount = _state.value.totalCount + files.size
        )

        uploadFiles(files = files)
    }

    private fun uploadFiles(files: List<ByteArray>) {
        Logger.info(message = "CreateIssueViewModel: uploadFiles")
        viewModelScope.launch {
            coroutineScope {
                files.map { bytes ->
                    async {
                        try {
                            val url: String = repository.uploadImage(bytes = bytes)
                            _state.update {
                                it.copy(
                                    uploadingCount = it.uploadingCount + 1,
                                    uploadedUrls = it.uploadedUrls + url
                                )
                            }
                            body.setValue(
                                body.value() + " ![image]($url) "
                            )
                        } catch (error: Exception) {
                            handleError(error = error)
                        }
                    }
                }.awaitAll()
            }
        }
    }

    data class State(
        val isLoading: Boolean = false,
        val isExpanded: Boolean = false,
        val uploadingCount: Int = 0,
        val totalCount: Int = 0,
        val uploadedUrls: List<String> = emptyList()
    )

    sealed interface Action {
        data class ShowError(val error: ErrorModel) : Action
        object RouteBack : Action
        object OpenImagePicker : Action
    }
}
