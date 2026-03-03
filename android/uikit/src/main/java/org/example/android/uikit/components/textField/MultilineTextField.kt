package org.example.android.uikit.components.textField

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults.FocusedBorderThickness
import androidx.compose.material3.OutlinedTextFieldDefaults.UnfocusedBorderThickness
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.android.uikit.components.utils.FieldBackground
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme
import kotlin.math.min

@Composable
fun MultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minHeight: Dp = 88.dp,
    maxHeight: Dp = Dp.Unspecified,
    maxSymbols: Int = -1,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = AppTheme.typography.body.large,
    colors: TextFieldColors = AppTheme.componentColors.textFieldColors,
) {
    var fieldHeight by remember { mutableIntStateOf(0) }
    var textHeight by remember { mutableIntStateOf(0) }

    val (limitText, visualTransformation) = when {
        maxSymbols > 0 -> {
            val limitText = "${value.length}/$maxSymbols"
            val fieldPaddings = with(LocalDensity.current) { 32.dp.toPx() }
            val oneLineHeight = with(LocalDensity.current) { textStyle.lineHeight.toPx() }
            val textSpaceHeight = fieldHeight - fieldPaddings - oneLineHeight

            val transformation = if (textHeight >= textSpaceHeight) {
                LimitTextPaddingVisualTransformation(limitText.length)
            } else {
                null
            }

            limitText to transformation
        }

        else -> null to null
    }

    BaseTextField(
        modifier = modifier
            .heightIn(min = minHeight, max = maxHeight)
            .onSizeChanged { size -> fieldHeight = size.height },
        value = value,
        onValueChange = { newValue ->
            if (maxSymbols <= 0 || newValue.length <= maxSymbols) {
                onValueChange(newValue)
            }
        },
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        singleLine = false,
        errorText = errorText,
        isError = isError,
        onTextLayout = { textHeight = it.size.height },
        interactionSource = interactionSource,
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        colors = colors,
        container = {
            MultilineTextFieldContainer(
                limitText = limitText,
                enabled = enabled,
                isError = isError,
                colors = colors,
                textStyle = textStyle,
                interactionSource = interactionSource,
            )
        },
    )
}

@Composable
private fun MultilineTextFieldContainer(
    limitText: String?,
    enabled: Boolean,
    isError: Boolean,
    textStyle: TextStyle,
    colors: TextFieldColors,
    interactionSource: InteractionSource,
    modifier: Modifier = Modifier,
    focusedBorderThickness: Dp = FocusedBorderThickness,
    unfocusedBorderThickness: Dp = UnfocusedBorderThickness,
) {
    val focused = interactionSource.collectIsFocusedAsState().value
    val indicatorColor = when {
        !enabled -> colors.disabledIndicatorColor
        isError -> colors.errorIndicatorColor
        focused -> colors.focusedIndicatorColor
        else -> colors.unfocusedIndicatorColor
    }
    val containerColor = when {
        !enabled -> colors.disabledContainerColor
        isError -> colors.errorContainerColor
        focused -> colors.focusedContainerColor
        else -> colors.unfocusedContainerColor
    }
    val limitColor = when {
        !enabled -> colors.disabledTrailingIconColor
        isError -> colors.errorTrailingIconColor
        focused -> colors.focusedTrailingIconColor
        else -> colors.unfocusedTrailingIconColor
    }

    FieldBackground(
        enabled = enabled,
        focused = focused,
        shape = AppTheme.shapes.m,
        indicatorColor = indicatorColor,
        containerColor = containerColor,
        modifier = modifier,
        focusedBorderThickness = focusedBorderThickness,
        unfocusedBorderThickness = unfocusedBorderThickness,
        content = limitText?.let {
            {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(all = 16.dp),
                    text = limitText,
                    color = limitColor,
                    style = textStyle
                )
            }
        }
    )
}

@Immutable
private class LimitTextPaddingVisualTransformation(
    private val limitTextLength: Int,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val paddingText = buildString {
            repeat(limitTextLength) {
                append('\u2007') // non-breaking fixed space
            }
        }

        val transformed = AnnotatedString(
            text.text + paddingText
        )

        // Map original offset to itself
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset
            override fun transformedToOriginal(offset: Int): Int = min(offset, text.text.length)
        }

        return TransformedText(transformed, offsetMapping)
    }
}

@MultiPreview
@Composable
private fun MultilineTextFieldPreview() = PreviewColumn {
    MultilineTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        label = "Название",
    )

    MultilineTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "Какое-то короткое название",
        onValueChange = {},
        label = "Название",
    )

    MultilineTextField(
        modifier = Modifier.fillMaxWidth(),
        maxSymbols = 250,
        value = "Какое-то очень длинное название в текстовом поле",
        onValueChange = {},
        label = "Название",
    )

    MultilineTextField(
        modifier = Modifier.fillMaxWidth(),
        maxSymbols = 250,
        value = "Какое-то очень длинное название в текстовом поле с ошибкой",
        onValueChange = {},
        label = "Название",
        errorText = "Некорректное название",
    )

    MultilineTextField(
        modifier = Modifier.fillMaxWidth(),
        maxSymbols = 250,
        value = "Какое-то очень и очень длинное название в многострочном текстовом поле, " +
            "которое не помещается в поле, занимает очень много строк и растягивает это поле вертикально",
        onValueChange = {},
        label = "Название",
    )
}
