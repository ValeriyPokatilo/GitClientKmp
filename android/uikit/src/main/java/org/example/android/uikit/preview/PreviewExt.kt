package org.example.android.uikit.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.flowOf
import org.example.android.uikit.theme.AppTheme

@Preview(
    name = "Light",
    group = "theme",
    showBackground = true,
    backgroundColor = 0xFFFFFBFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    group = "theme",
    showBackground = true,
    backgroundColor = 0xFF1B1B23,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class MultiPreview

@Preview(
    name = "Light",
    group = "theme",
    widthDp = 500,
    showBackground = true,
    backgroundColor = 0xFFFFFBFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    group = "theme",
    widthDp = 500,
    showBackground = true,
    backgroundColor = 0xFF1B1B23,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class MultiPreview500

@Preview(
    name = "Light",
    group = "theme",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFBFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    group = "theme",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFF1B1B23,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class MultiUiPreview

@Composable
fun PreviewBody(content: @Composable BoxScope.() -> Unit) {
    AppTheme {
        Box(
            modifier = Modifier.background(AppTheme.colors.surface),
            content = content
        )
    }
}

@Composable
fun PreviewColumn(
    spacedBy: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    AppTheme {
        Column(
            modifier = Modifier.background(AppTheme.colors.surface),
            verticalArrangement = Arrangement.spacedBy(spacedBy),
            content = content
        )
    }
}

@Composable
fun PreviewRow(
    spacedBy: Dp = 8.dp,
    content: @Composable RowScope.() -> Unit
) {
    AppTheme {
        Row(
            modifier = Modifier.background(AppTheme.colors.surface),
            horizontalArrangement = Arrangement.spacedBy(spacedBy),
            content = content
        )
    }
}

fun previewInteractionSource(isPressed: Boolean = false): MutableInteractionSource {
    val enterInteraction = HoverInteraction.Enter()
    val pressInteraction = PressInteraction.Press(Offset(0f, 0f))

    val interactions = if (isPressed) {
        flowOf(enterInteraction, pressInteraction)
    } else {
        flowOf(enterInteraction)
    }

    return object : MutableInteractionSource {
        override val interactions = interactions
        override suspend fun emit(interaction: Interaction) = Unit

        override fun tryEmit(interaction: Interaction): Boolean {
            return true
        }
    }
}
