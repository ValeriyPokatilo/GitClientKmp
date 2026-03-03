package org.example.android.uikit.components.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.android.uikit.components.snackBar.DefaultSnackBar
import org.example.android.uikit.theme.AppTheme
import org.example.android.utils.safePadding

@Composable
fun BaseToolbarScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null,
    containerColor: Color = AppTheme.colors.background,
    content: @Composable BoxScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .imePadding(),
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        DefaultSnackBar(
                            message = data.visuals.message
                        )
                    }
                )
            }
        },
        topBar = topBar,
        containerColor = containerColor,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safePadding(innerPadding),
            contentAlignment = Alignment.TopCenter,
            content = content
        )
    }
}
