package org.example.android.uikit.components.scaffold

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.android.uikit.R
import org.example.android.uikit.components.topAppBar.TopAppBarIcon
import org.example.android.uikit.preview.PreviewBody
import org.example.android.utils.ActionDebouncer
import org.example.android.utils.rememberActionDebouncer

@Composable
fun CloseToolbarScaffold(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    toolbarTrailing: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val debouncer: ActionDebouncer = rememberActionDebouncer()

    DefaultToolbarScaffold(
        modifier = modifier,
        title = title,
        snackbarHostState = snackbarHostState,
        toolbarLeading = {
            TopAppBarIcon(
                painter = painterResource(R.drawable.ic_close),
                onClick = {
                    debouncer.performAction(onCloseClick)
                }
            )
        },
        toolbarTrailing = toolbarTrailing,
        content = content
    )
}

@PreviewLightDark
@Composable
private fun CloseToolbarScaffoldPreview() = PreviewBody {
    CloseToolbarScaffold(
        title = "Новое объявление",
        onCloseClick = {},
    ) {
        Text("Content here")
    }
}
