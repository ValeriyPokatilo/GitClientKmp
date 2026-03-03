package org.example.android.uikit.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.localized
import org.example.android.uikit.state.model.EmptyStateData
import org.example.android.uikit.state.model.ErrorStateData
import org.example.android.uikit.state.model.LoadingStateData
import org.example.library.utils.state.RemoteState
import org.example.library.utils.state.RemoteStateUi

@Composable
fun <T : Any> RemoteStateContent(
    modifier: Modifier = Modifier,
    state: RemoteStateUi<T>,
    loadingData: LoadingStateData,
    errorData: ErrorStateData,
    onRetryClick: () -> Unit,
    content: @Composable (T) -> Unit,
) {
    when (state) {
        is RemoteState.Error -> {
            ErrorStateContent(
                modifier = modifier,
                text = state.error.localized(),
                buttonText = errorData.buttonText,
                onRetryClick = onRetryClick
            )
        }

        is RemoteState.Loading -> {
            LoadingStateContent(
                modifier = modifier,
                data = loadingData,
            )
        }

        is RemoteState.Success -> {
            content(state.data)
        }
    }
}

@Composable
fun <T : Any> RemoteStateContent(
    modifier: Modifier = Modifier,
    state: RemoteStateUi<T>,
    emptyCondition: (T) -> Boolean,
    emptyContent: @Composable () -> Unit,
    loadingData: LoadingStateData,
    errorData: ErrorStateData,
    onRetryClick: () -> Unit,
    content: @Composable (T) -> Unit,
) {
    RemoteStateContent(
        modifier = modifier,
        state = state,
        loadingData = loadingData,
        errorData = errorData,
        onRetryClick = onRetryClick,
        content = { data ->
            if (emptyCondition(data)) {
                emptyContent()
            } else {
                content(data)
            }
        }
    )
}

@Composable
fun <T : Any> RemoteStateContent(
    modifier: Modifier = Modifier,
    state: RemoteStateUi<T>,
    emptyCondition: (T) -> Boolean,
    emptyData: EmptyStateData,
    loadingData: LoadingStateData,
    errorData: ErrorStateData,
    onRetryClick: () -> Unit,
    content: @Composable (T) -> Unit,
) {
    RemoteStateContent(
        modifier = modifier,
        state = state,
        emptyCondition = emptyCondition,
        emptyContent = {
            EmptyStateContent(
                data = emptyData,
                onReload = onRetryClick,
            )
        },
        loadingData = loadingData,
        errorData = errorData,
        onRetryClick = onRetryClick,
        content = content
    )
}
