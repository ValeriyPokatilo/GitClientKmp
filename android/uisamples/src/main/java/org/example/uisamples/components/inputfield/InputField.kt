package org.example.uisamples.components.inputfield

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.android.uisamples.R
import org.example.uisamples.components.inputfield.decoration.InputFieldDecorationBox
import org.example.uisamples.themes.B

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String? = null,
    placeholder: String? = null,
    description: String? = null,
    enabled: Boolean = true,
    error: String? = null,
    success: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    InputField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValueState = newText
            if (value != newText.text) {
                onValueChange(newText.text)
            }
        },
        label = label,
        placeholder = placeholder,
        description = description,
        enabled = enabled,
        error = error,
        success = success,
        maxLength = maxLength,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        singleLine = singleLine,
        interactionSource = interactionSource,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        colors = colors,
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit = {},
    label: String? = null,
    placeholder: String? = null,
    description: String? = null,
    enabled: Boolean = true,
    error: String? = null,
    success: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {

    val isError by remember(error) {
        mutableStateOf(error != null)
    }

    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            if (it.text.length <= maxLength || it.text.length <= value.text.length) {
                onValueChange(
                    it.copy(
                        text = it.text
                    )
                )
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            InputFieldDecorationBox(
                value = value.text,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                label = label,
                placeholder = placeholder,
                error = error,
                success = success,
                isError = isError,
                description = description,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                prefix = prefix,
                suffix = suffix,
                colors = colors
            )
        }
    )
}

@Suppress("LongMethod")
@Composable
private fun SimpleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String? = null,
    enabled: Boolean = true,
    error: String? = null,
    singleLine: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = B.colors.primary,
                style = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp)
                .border(
                    width = 1.dp,
                    color = B.colors.borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 8.dp,
                                top = 12.dp,
                                bottom = 12.dp,
                            )
                    ) {
                        leadingIcon()
                    }
                }
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                end = 16.dp,
                                top = 12.dp,
                                bottom = 12.dp,
                            )
                    ) {
                        trailingIcon()
                    }
                }
            }
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        start = if (leadingIcon != null) 40.dp else 16.dp,
                        end = if (trailingIcon != null) 40.dp else 16.dp,
                    ),
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = false,
                singleLine = singleLine,
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 22.dp)
        ) {
            if (error != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    text = error,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = B.colors.primaryRed,
                    style = TextStyle(
                        fontWeight = FontWeight.W500,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SimpleTextFieldFullPreview() {
    var textFieldValue: String by remember {
        mutableStateOf("testvalue")
    }
    SimpleTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = 8.dp
            ),
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValue = newText
        },
        label = "Внезапно супердлинное по названию описание для тестирования лейбла",
        error = "ошибка для тестирования отображения длинной ошибки",
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        },
    )
}

@PreviewLightDark
@Composable
private fun MaskedTextFieldPreview() {
    var textFieldValue: String by remember {
        mutableStateOf("testvalue")
    }
    InputField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = 8.dp
            ),
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValue = newText
        },
        label = "Label",
        placeholder = "Placeholder",
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .size(16.dp),
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = null
            )
        },
        error = "ошибка для тестирования",
    )
}

@PreviewLightDark
@Composable
private fun MaskedTextFieldFullPreview() {
    var textFieldValue: String by remember {
        mutableStateOf("testvalue")
    }
    InputField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = 8.dp
            ),
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValue = newText
        },
        label = "Внезапно супердлинное по названию описание для тестирования лейбла и немного текста еще для теста",
        description = "тестовое описание",
        error = "ошибка для тестирования отображения длинной ошибки",
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        },
    )
}

@PreviewLightDark
@Composable
private fun MaskedTextFieldCompactPreview() {
    var textFieldValue: String by remember {
        mutableStateOf("")
    }
    InputField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = 8.dp
            ),
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValue = newText
        },
        label = null,
        description = null,
        error = null,
    )
}
