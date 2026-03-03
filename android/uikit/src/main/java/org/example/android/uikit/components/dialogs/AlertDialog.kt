package org.example.android.uikit.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.android.uikit.R
import org.example.android.uikit.components.button.DefaultButton
import org.example.android.uikit.components.button.DefaultButtonStyle
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun AlertDialog(
    text: String,
    primaryButton: String,
    secondaryButton: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    onDismiss: () -> Unit,
    icon: Painter? = null,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
        )
    ) {
        AlertDialogContent(
            icon = icon,
            text = text,
            primaryButton = primaryButton,
            onPrimaryClick = onPrimaryClick,
            secondaryButton = secondaryButton,
            onSecondaryClick = onSecondaryClick,
        )
    }
}

@Composable
private fun AlertDialogContent(
    text: String,
    primaryButton: String,
    secondaryButton: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    icon: Painter? = null,
) {
    Column(
        modifier = Modifier
            .background(
                color = AppTheme.colors.surface,
                shape = AppTheme.shapes.xxl,
            )
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (icon != null) {
            Image(
                modifier = Modifier.size(80.dp),
                painter = icon,
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = text,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.onSurface,
            style = AppTheme.typography.title.large,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DefaultButton(
                modifier = Modifier.weight(1f),
                text = secondaryButton,
                onClick = onSecondaryClick,
                style = DefaultButtonStyle.Outlined,
            )

            DefaultButton(
                modifier = Modifier.weight(1f),
                text = primaryButton,
                onClick = onPrimaryClick,
                style = DefaultButtonStyle.Filled,
            )
        }
    }
}

@MultiPreview
@Composable
private fun AlertDialogPreview() = PreviewBody {
    AlertDialogContent(
        text = "Вы готовы продолжить?",
        primaryButton = "Да",
        secondaryButton = "Нет",
        icon = painterResource(R.drawable.ic_email),
        onPrimaryClick = {},
        onSecondaryClick = {},
    )
}
