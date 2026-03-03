package org.example.android.uikit.components.snackBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultSnackBar(
    message: String
) {
    Snackbar(
        modifier = Modifier
            .heightIn(min = 48.dp)
            .fillMaxWidth()
            .padding(bottom = 98.dp, start = 16.dp, end = 16.dp)
            .clip(AppTheme.shapes.m),
        containerColor = AppTheme.colors.inverseSurface,
        content = {
            Text(
                text = message,
                style = AppTheme.typography.label.medium,
                color = AppTheme.colors.inverseOnSurface
            )
        }
    )
}
