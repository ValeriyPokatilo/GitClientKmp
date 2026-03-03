package org.example.uisamples.components.textfields

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.uisamples.themes.GgcTheme

@Composable
fun GgcTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    leadingIconPainter: Painter? = null,
    trailingIconPainter: Painter? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    colors: TextFieldColors = GgcTheme.textFieldColors,
    onTrailingButtonClick: () -> Unit = {},
) {
    val isError = error != null
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = when {
                        isError -> colorScheme.error
                        isFocused -> colorScheme.primary
                        else -> Color.Transparent
                    },
                    shape = RoundedCornerShape(16.dp)
                ),
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = interactionSource,
            value = value,
            label = label,
            prefix = prefix,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            singleLine = singleLine,
            placeholder = placeholder,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = leadingIconPainter?.textFieldIcon(),
            trailingIcon = trailingIconPainter?.textFieldIcon(onTrailingButtonClick),
            colors = colors,
            visualTransformation = visualTransformation
        )

        error?.let {
            Text(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                text = it,
                style = GgcTheme.typography.textFieldError.copy(
                    color = colorScheme.error
                )
            )
        }
    }
}

@Composable
internal fun Painter.textFieldIcon(onClick: (() -> Unit)? = null): (@Composable () -> Unit) {
    return {
        val interactionSource = remember { MutableInteractionSource() }
        val iconModifier = if (onClick != null) {
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
        } else {
            Modifier
        }
        Icon(
            modifier = iconModifier,
            painter = this,
            contentDescription = null
        )
    }
}

@PreviewLightDark
@Composable
private fun GgcTextFieldPreview() {
    GgcTheme {
        GgcTextField(
            modifier = Modifier,
            value = "value",
            onValueChange = {},
            error = null,
            enabled = true,
            singleLine = true,
            readOnly = false,
            label = null,
            placeholder = null,
        )
    }
}
