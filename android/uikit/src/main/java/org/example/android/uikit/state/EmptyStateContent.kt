package org.example.android.uikit.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.components.button.DefaultButton
import org.example.android.uikit.components.button.DefaultButtonStyle
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.state.model.EmptyStateData
import org.example.android.uikit.theme.AppTheme

@Composable
fun EmptyStateContent(
    data: EmptyStateData,
    onReload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmptyStateContent(
        modifier = modifier,
        title = data.title,
        text = data.text,
        buttonText = data.buttonText,
        icon = data.icon,
        onReload = onReload,
    )
}

@Composable
fun EmptyStateContent(
    title: String,
    text: String,
    buttonText: String,
    onReload: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
) {
    Column(
        modifier = modifier.padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (icon != null) {
            Image(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .size(80.dp),
                painter = icon,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = AppTheme.typography.title.large.copy(
                fontWeight = FontWeight.W600,
            )
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 10.dp),
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = AppTheme.typography.title.large.copy(
                fontWeight = FontWeight.W400,
            )
        )

        DefaultButton(
            text = buttonText,
            onClick = onReload,
            style = DefaultButtonStyle.Filled,
        )
    }
}

@MultiPreview
@Composable
private fun EmptyStateContentPreview() = PreviewBody {
    EmptyStateContent(
        modifier = Modifier.height(800.dp),
        title = "Пусто",
        text = "В этом разделе пока пусто.",
        buttonText = "Обновить",
        icon = painterResource(R.drawable.ic_email),
        onReload = {}
    )
}
