package org.example.android.uikit.components.scaffold

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.android.uikit.components.topAppBar.DefaultTopAppBar
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultToolbarScaffold(
    title: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    toolbarLeading: @Composable (BoxScope.() -> Unit)? = null,
    toolbarTrailing: @Composable (BoxScope.() -> Unit)? = null,
    containerColor: Color = AppTheme.colors.background,
    content: @Composable BoxScope.() -> Unit,
) {
    BaseToolbarScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        containerColor = containerColor,
        topBar = {
            DefaultTopAppBar(
                title = title,
                leading = toolbarLeading,
                trailing = toolbarTrailing,
            )
        },
        content = content
    )
}

@PreviewLightDark
@Composable
private fun DefaultToolbarScaffoldPreview() = PreviewBody {
    DefaultToolbarScaffold(
        title = "ToolbarScaffold",
    ) {
        Text("Content here")
    }
}
