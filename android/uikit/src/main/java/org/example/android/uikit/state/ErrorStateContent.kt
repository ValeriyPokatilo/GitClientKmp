package org.example.android.uikit.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import org.example.android.uikit.theme.AppTheme

@Composable
fun ErrorStateContent(
    text: String,
    buttonText: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Painter? = null,
) {
    Column(
        modifier = modifier.padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon?.let {
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 10.dp),
                painter = icon,
                contentDescription = null
            )
        }

        title?.let {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onSurface,
                style = AppTheme.typography.title.large
            )
        }

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = AppTheme.typography.title.large
        )

        DefaultButton(
            modifier = Modifier.padding(top = 10.dp),
            text = buttonText,
            style = DefaultButtonStyle.Filled,
            onClick = onRetryClick,
        )
    }
}

@MultiPreview
@Composable
private fun ErrorStatePreview() = PreviewBody {
    ErrorStateContent(
        modifier = Modifier.height(800.dp),
        icon = painterResource(R.drawable.ic_email),
        title = "Ошибка загрузки",
        text = "При загрузке данных произошла ошибка.",
        buttonText = "Повторить",
        onRetryClick = {}
    )
}
