package org.example.uisamples.components.picker.time

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.example.uisamples.themes.GgcTheme

@Composable
fun WheelTextPicker(
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    size: DpSize = DpSize(128.dp, 128.dp),
    texts: List<String>,
    rowCount: Int,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = GgcTheme.typography.inputContent,
    onScrollFinished: (snappedIndex: Int) -> Int? = { null },
) {
    WheelPicker(
        modifier = modifier,
        startIndex = startIndex,
        size = size,
        count = texts.size,
        rowCount = rowCount,
        onScrollFinished = onScrollFinished
    ) { index ->
        Text(
            text = texts[index],
            style = style,
            color = color,
            maxLines = 1
        )
    }
}

@PreviewLightDark
@Composable
private fun WheelTextPickerPreview() {
    GgcTheme {
        WheelTextPicker(
            modifier = Modifier,
            startIndex = 0,
            texts = listOf(
                "test1",
                "test2",
                "test3",
                "test4",
                "test5",
            ),
            rowCount = 1,
        )
    }
}
