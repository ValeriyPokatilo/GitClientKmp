package org.example.android.uikit.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.state.model.LoadingStateData
import org.example.android.uikit.theme.AppTheme

@Composable
fun LoadingStateContent(
    data: LoadingStateData,
    modifier: Modifier = Modifier,
) {
    LoadingStateContent(
        modifier = modifier,
        text = data.text
    )
}

@Composable
fun LoadingStateContent(
    modifier: Modifier = Modifier,
    text: String? = null,
) {
    Column(
        modifier = modifier.padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()

        text?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onSurface,
                style = AppTheme.typography.title.large,
            )
        }
    }
}

@MultiPreview
@Composable
private fun LoadingStateContentPreview() = PreviewBody {
    LoadingStateContent(
        modifier = Modifier.height(800.dp),
        text = "Загрузка"
    )
}
