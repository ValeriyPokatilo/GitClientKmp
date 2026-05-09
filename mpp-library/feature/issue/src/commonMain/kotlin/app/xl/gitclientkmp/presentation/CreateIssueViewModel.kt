package app.xl.gitclientkmp.presentation

import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.error.ErrorModel
import app.xl.gitclientkmp.domain.repository.AppRepository
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
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                repository.createIssue(
                    ownerName = owner,
                    repositoryName = repositoryName,
                    title = title.value().trim(),
                    body = body.value().trim()
                )

                _action.emit(value = Action.RouteBackWithRefresh)
            } catch (error: Exception) {
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
        if (listOf(title, body).validate().not()) {
            return
        }

        createIssue()
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            _action.emit(value = Action.RouteBack)
        }
    }

    fun onAttachPressed() {
        viewModelScope.launch {
            _action.emit(value = Action.OpenImagePicker)
        }
    }

    fun onArrowPressed() {
        _state.value = _state.value.copy(
            isExpanded = !_state.value.isExpanded
        )
    }

    fun onFilesSelected(files: List<ByteArray>) {
        _state.value = _state.value.copy(
            isExpanded = true,
            totalCount = _state.value.totalCount + files.size
        )

        uploadFiles(files = files)
    }

    private fun uploadFiles(files: List<ByteArray>) {
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
                                body.value() + "\n\n![image]($url)"
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
        object RouteBackWithRefresh : Action
        object OpenImagePicker : Action
    }
}
