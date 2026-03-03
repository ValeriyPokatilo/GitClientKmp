package org.example.android.uikit.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.painterResource
import org.example.android.uikit.state.model.EmptyStateData
import org.example.android.uikit.state.model.ErrorStateData
import org.example.android.uikit.state.model.LoadingStateData
import org.example.library.utils.state.ErrorBundle

@Composable
fun <T : Any> ResourceStateContent(
    modifier: Modifier = Modifier,
    state: ResourceState<T, ErrorBundle>,
    loadingData: LoadingStateData,
    emptyData: EmptyStateData,
    errorData: ErrorStateData,
    onRetryClick: () -> Unit,
    content: @Composable (T) -> Unit,
) {
    when (state) {
        is ResourceState.Failed -> {
            ErrorStateContent(
                modifier = modifier,
                icon = painterResource(state.error.icon),
                title = state.error.title.localized(),
                text = state.error.message.localized(),
                buttonText = errorData.buttonText,
                onRetryClick = onRetryClick
            )
        }

        is ResourceState.Loading -> {
            LoadingStateContent(
                modifier = modifier,
                data = loadingData,
            )
        }

        is ResourceState.Empty -> {
            EmptyStateContent(
                modifier = modifier,
                data = emptyData,
                onReload = onRetryClick,
            )
        }

        is ResourceState.Success -> {
            content(state.data)
        }
    }
}
