package org.example.android.uikit.components.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.android.uikit.preview.MultiUiPreview
import org.example.android.uikit.preview.PreviewBody

@Composable
fun CenteredProgressBar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@MultiUiPreview
private fun CenteredProgressBarPreview() = PreviewBody {
    CenteredProgressBar()
}
