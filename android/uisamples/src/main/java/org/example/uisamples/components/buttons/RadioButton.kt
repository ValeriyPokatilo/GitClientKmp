package org.example.uisamples.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.android.uisamples.R
import org.example.uisamples.themes.GgcTheme

@Composable
fun RadioButtonItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = GgcTheme.typography.description.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.W500
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun RadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Image(
        painter = painterResource(
            if (selected) R.drawable.ic_radiobutton_selected else R.drawable.ic_radiobutton_unselected
        ),
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
    )
}

@Composable
@PreviewLightDark
fun RadioButtonItemPreview() {
    GgcTheme {
        RadioButtonItem(
            text = "Test",
            selected = false,
            onClick = {}
        )
    }
}

@Composable
@PreviewLightDark
fun RadioButtonPreview() {
    GgcTheme {
        RadioButton(
            selected = false,
            onClick = {}
        )
    }
}
