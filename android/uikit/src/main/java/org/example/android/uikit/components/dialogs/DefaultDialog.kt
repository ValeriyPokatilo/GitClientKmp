package org.example.android.uikit.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
fun DefaultDialog(
    text: String,
    primaryButton: String,
    onPrimaryClick: () -> Unit,
    onDismiss: () -> Unit,
    title: String? = null,
    icon: Painter? = null,
    primaryButtonStyle: DefaultButtonStyle = DefaultButtonStyle.Filled,
    secondaryButtonStyle: DefaultButtonStyle = DefaultButtonStyle.Outlined,
    secondaryButton: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
        )
    ) {
        DefaultDialogContent(
            icon = icon,
            title = title,
            text = text,
            primaryButton = primaryButton,
            primaryButtonStyle = primaryButtonStyle,
            onPrimaryClick = onPrimaryClick,
            secondaryButton = secondaryButton,
            secondaryButtonStyle = secondaryButtonStyle,
            onSecondaryClick = onSecondaryClick,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun DefaultDialogContent(
    text: String,
    primaryButton: String,
    onPrimaryClick: () -> Unit,
    title: String? = null,
    icon: Painter? = null,
    primaryButtonStyle: DefaultButtonStyle = DefaultButtonStyle.Filled,
    secondaryButtonStyle: DefaultButtonStyle = DefaultButtonStyle.Outlined,
    secondaryButton: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .background(
                color = AppTheme.colors.surface,
                shape = AppTheme.shapes.xxl,
            )
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (icon != null) {
            Image(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(80.dp),
                painter = icon,
                contentDescription = null,
            )
        }

        if (title != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                text = title,
                textAlign = TextAlign.Center,
                color = AppTheme.colors.onSurface,
                style = AppTheme.typography.title.large,
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = text,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.onSurface,
            style = AppTheme.typography.label.medium,
        )

        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            text = primaryButton,
            onClick = onPrimaryClick,
            style = primaryButtonStyle,
        )

        if (secondaryButton != null && onSecondaryClick != null) {
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = secondaryButton,
                onClick = onSecondaryClick,
                style = secondaryButtonStyle,
            )
        }
    }
}

@MultiPreview
@Composable
private fun DefaultDialogPreview() = PreviewBody {
    DefaultDialogContent(
        title = "Ошибка",
        text = "Ошибка соединения с сервером",
        primaryButton = "Повторить попытку",
        secondaryButton = "Отмена",
        icon = painterResource(R.drawable.ic_email),
        onPrimaryClick = {},
        onSecondaryClick = {},
    )
}
