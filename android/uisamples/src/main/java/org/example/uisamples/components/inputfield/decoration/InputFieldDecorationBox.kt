package org.example.uisamples.components.inputfield.decoration

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.uisamples.components.inputfield.layout.TextFieldLayout
import org.example.uisamples.components.inputfield.layout.TextFieldLayoutDefaults
import org.example.uisamples.themes.B

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun InputFieldDecorationBox(
    value: String,
    innerTextField: @Composable () -> Unit,
    enabled: Boolean = true,
    singleLine: Boolean,
    visualTransformation: VisualTransformation,
    interactionSource: InteractionSource,
    label: String? = null,
    placeholder: String? = null,
    error: String? = null,
    success: String? = null,
    isError: Boolean = false,
    description: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(),
    contentPadding: PaddingValues =
        PaddingValues(horizontal = 12.dp, vertical = 12.dp),
    container: @Composable () -> Unit = {
        Box(
            modifier = Modifier.background(
                color = Color.Transparent,
                shape = shape
            )
        )
    }
) {
    val transformedText: String = remember(key1 = value, key2 = visualTransformation) {
        visualTransformation.filter(AnnotatedString(value))
    }.text.text

    val isFocused: Boolean = interactionSource.collectIsFocusedAsState().value
    val inputState: InputPhase = when {
        isFocused -> InputPhase.Focused
        transformedText.isEmpty() -> InputPhase.UnfocusedEmpty
        else -> InputPhase.UnfocusedNotEmpty
    }

    val labelColor: @Composable (InputPhase) -> Color = {
        if (!enabled) {
            B.colors.darkGrey
        } else {
            if (isFocused) {
                colors.focusedLabelColor
            } else {
                B.colors.grey20
            }
        }
    }

    val borderColor: Color = if (isFocused) {
        colors.focusedLabelColor
    } else {
        if (isError) {
            B.colors.primaryRed
        } else {
            B.colors.borderColor
        }
    }
    val containerBackground: Color = if (enabled) {
        Color.Transparent
    } else {
        B.colors.surface95
    }

    TextFieldTransitionScope.Transition(
        inputState = inputState,
        focusedTextStyleColor = labelColor(inputState),
        unfocusedTextStyleColor = labelColor(inputState),
        contentColor = labelColor,
        showLabel = label != null
    ) { labelProgress, labelTextStyleColor, labelContentColor, placeholderAlphaProgress,
        prefixSuffixAlphaProgress ->

        val decoratedLabel: @Composable (() -> Unit)? = label?.let {
            @Composable {

                Decoration(labelContentColor) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = labelTextStyleColor,
                        style = TextStyle(
                            fontWeight = FontWeight.W500,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                        )
                    )
                }
            }
        }

        val decoratedPlaceholder: @Composable ((Modifier) -> Unit)? =
            if (placeholder != null && transformedText.isEmpty() && placeholderAlphaProgress > 0f) {
                @Composable { modifier ->
                    Box(modifier.alpha(placeholderAlphaProgress)) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = placeholder,
                            color = B.colors.grey20,
                            style = TextStyle(
                                fontWeight = FontWeight.W500,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            } else {
                null
            }

        val decoratedPrefix: @Composable (() -> Unit)? =
            if (prefix != null && prefixSuffixAlphaProgress > 0f) {
                @Composable {
                    Box(Modifier.alpha(prefixSuffixAlphaProgress)) {
                    }
                }
            } else {
                null
            }

        val decoratedSuffix: @Composable (() -> Unit)? =
            if (suffix != null && prefixSuffixAlphaProgress > 0f) {
                @Composable {
                    Box(Modifier.alpha(prefixSuffixAlphaProgress))
                }
            } else {
                null
            }

        val decoratedLeading: @Composable (() -> Unit)? = leadingIcon?.let {
            @Composable {
                Decoration(contentColor = labelContentColor, content = it)
            }
        }

        val decoratedTrailing: @Composable (() -> Unit)? = trailingIcon?.let {
            @Composable {
                Decoration(contentColor = labelContentColor, content = it)
            }
        }

        val containerWithId: @Composable () -> Unit = {
            Box(
                Modifier
                    .layoutId(TextFieldLayoutDefaults.CONTAINER_ID)
                    .defaultMinSize(minHeight = 40.dp)
                    .background(
                        color = containerBackground,
                        shape = shape
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = shape
                    ),
                propagateMinConstraints = true
            ) {
                container()
            }
        }

        TextFieldLayout(
            modifier = Modifier,
            textField = innerTextField,
            placeholder = decoratedPlaceholder,
            label = decoratedLabel,
            leading = decoratedLeading,
            trailing = decoratedTrailing,
            prefix = decoratedPrefix,
            suffix = decoratedSuffix,
            container = containerWithId,
            supporting = {
                Column {
                    error?.let {
                        SimpleTextFieldDescriptionIndicator(
                            text = it,
                            color = B.colors.primaryRed,
                        )
                    }
                    success?.let {
                        if (error != null) Spacer(Modifier.height(4.dp))
                        SimpleTextFieldDescriptionIndicator(
                            text = it,
                        )
                    }
                    description?.let {
                        if (error != null || success != null) Spacer(Modifier.height(4.dp))
                        SimpleTextFieldDescriptionIndicator(
                            text = it,
                        )
                    }
                }
            },
            singleLine = singleLine,
            animationProgress = labelProgress,
            paddingValues = contentPadding
        )
    }
}

internal enum class InputPhase {
    // Text field is focused
    Focused,

    // Text field is not focused and input text is empty
    UnfocusedEmpty,

    // Text field is not focused but input text is not empty
    UnfocusedNotEmpty
}

private object TextFieldTransitionScope {
    @Suppress("LongMethod", "CyclomaticComplexMethod")
    @Composable
    fun Transition(
        inputState: InputPhase,
        focusedTextStyleColor: Color,
        unfocusedTextStyleColor: Color,
        contentColor: @Composable (InputPhase) -> Color,
        showLabel: Boolean,
        content: @Composable (
            labelProgress: Float,
            labelTextStyleColor: Color,
            labelContentColor: Color,
            placeholderOpacity: Float,
            prefixSuffixOpacity: Float,
        ) -> Unit
    ) {
        val transition = updateTransition(inputState, label = "TextFieldInputState")

        val labelProgress by transition.animateFloat(
            label = "LabelProgress",
            transitionSpec = { tween(durationMillis = 150) }
        ) {
            when (it) {
                InputPhase.Focused -> 1f
                InputPhase.UnfocusedEmpty -> 0f
                InputPhase.UnfocusedNotEmpty -> 1f
            }
        }

        val placeholderOpacity by transition.animateFloat(
            label = "PlaceholderOpacity",
            transitionSpec = {
                if (InputPhase.Focused isTransitioningTo InputPhase.UnfocusedEmpty) {
                    tween(
                        durationMillis = 67,
                        easing = LinearEasing
                    )
                } else if (InputPhase.UnfocusedEmpty isTransitioningTo InputPhase.Focused ||
                    InputPhase.UnfocusedNotEmpty isTransitioningTo InputPhase.UnfocusedEmpty
                ) {
                    tween(
                        durationMillis = 150,
                        delayMillis = 67,
                        easing = LinearEasing
                    )
                } else {
                    spring()
                }
            }
        ) {
            when (it) {
                InputPhase.Focused -> 1f
                InputPhase.UnfocusedEmpty -> 1f
                InputPhase.UnfocusedNotEmpty -> 0f
            }
        }

        val prefixSuffixOpacity by transition.animateFloat(
            label = "PrefixSuffixOpacity",
            transitionSpec = { tween(durationMillis = TextFieldLayoutDefaults.ANIMATION_DURATION) }
        ) {
            when (it) {
                InputPhase.Focused -> 1f
                InputPhase.UnfocusedEmpty -> if (showLabel) 0f else 1f
                InputPhase.UnfocusedNotEmpty -> 1f
            }
        }

        val labelTextStyleColor by transition.animateColor(
            transitionSpec = { tween(durationMillis = TextFieldLayoutDefaults.ANIMATION_DURATION) },
            label = "LabelTextStyleColor"
        ) {
            when (it) {
                InputPhase.Focused -> focusedTextStyleColor
                else -> unfocusedTextStyleColor
            }
        }

        val labelContentColor by transition.animateColor(
            transitionSpec = { tween(durationMillis = TextFieldLayoutDefaults.ANIMATION_DURATION) },
            label = "LabelContentColor",
            targetValueByState = contentColor
        )

        content(
            labelProgress,
            labelTextStyleColor,
            labelContentColor,
            placeholderOpacity,
            prefixSuffixOpacity,
        )
    }
}

@Composable
private fun Decoration(
    contentColor: Color,
    content: @Composable () -> Unit
) {
    val contentWithColor: @Composable () -> Unit = @Composable {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            content = content
        )
    }
    contentWithColor()
}

@Composable
private fun SimpleTextFieldDescriptionIndicator(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = B.colors.grey70,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    )
}
