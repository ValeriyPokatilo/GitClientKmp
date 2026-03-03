package org.example.uisamples.components.inputfield.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

@Suppress("LongMethod")
@Composable
internal fun TextFieldLayout(
    modifier: Modifier,
    textField: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable ((Modifier) -> Unit)?,
    leading: @Composable (() -> Unit)?,
    trailing: @Composable (() -> Unit)?,
    prefix: @Composable (() -> Unit)?,
    suffix: @Composable (() -> Unit)?,
    singleLine: Boolean,
    animationProgress: Float,
    container: @Composable () -> Unit,
    supporting: @Composable (() -> Unit)?,
    paddingValues: PaddingValues
) {
    val measurePolicy = remember(singleLine, animationProgress, paddingValues) {
        TextFieldMeasurePolicy(animationProgress, paddingValues)
    }
    val layoutDirection = LocalLayoutDirection.current
    Layout(
        modifier = modifier,
        content = {
            // The container is given as a Composable instead of a background modifier so that
            // elements like supporting text can be placed outside of it while still contributing
            // to the text field's measurements overall.
            container()

            if (label != null) {
                Box(
                    Modifier
                        .layoutId(TextFieldLayoutDefaults.LABEL_ID)
                        .wrapContentHeight()
                ) { label() }
            }

            val startTextFieldPadding = paddingValues.calculateStartPadding(layoutDirection)
            val endTextFieldPadding = paddingValues.calculateEndPadding(layoutDirection)

            if (leading != null) {
                Box(
                    modifier = Modifier
                        .layoutId(TextFieldLayoutDefaults.LEADING_ID)
                        .padding(start = startTextFieldPadding)
                        .then(Modifier.defaultMinSize(16.dp, 16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    leading()
                }
            }
            if (trailing != null) {
                Box(
                    modifier = Modifier
                        .layoutId(TextFieldLayoutDefaults.TRAILING_ID)
                        .padding(end = endTextFieldPadding)
                        .then(Modifier.defaultMinSize(16.dp, 16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    trailing()
                }
            }

            val startPadding: Dp = if (leading != null) {
                (startTextFieldPadding - 12.dp).coerceAtLeast(
                    0.dp
                )
            } else {
                startTextFieldPadding
            }
            val endPadding: Dp = if (trailing != null) {
                (endTextFieldPadding - 12.dp).coerceAtLeast(
                    0.dp
                )
            } else {
                endTextFieldPadding
            }

            if (prefix != null) {
                Box(
                    Modifier
                        .layoutId(LAYOUT_ID_PREFIX)
                        .wrapContentHeight()
                        .padding(
                            start = startPadding,
                            end = 2.dp
                        )
                ) {
                    prefix()
                }
            }
            if (suffix != null) {
                Box(
                    Modifier
                        .layoutId(LAYOUT_ID_SUFFIX)
                        .wrapContentHeight()
                        .padding(
                            start = 2.dp,
                            end = endPadding
                        )
                ) {
                    suffix()
                }
            }

            val textPadding = Modifier
                .wrapContentHeight()
                .padding(
                    start = if (prefix == null) startPadding else 0.dp,
                    end = if (suffix == null) endPadding else 0.dp,
                )

            if (placeholder != null) {
                placeholder(
                    Modifier
                        .layoutId(TextFieldLayoutDefaults.PLACEHOLDER_ID)
                        .then(textPadding)
                )
            }
            Box(
                modifier = Modifier
                    .layoutId(TextFieldLayoutDefaults.TEXT_FIELD_ID)
                    .then(textPadding),
                propagateMinConstraints = true,
            ) {
                textField()
            }

            if (supporting != null) {
                Box(
                    Modifier
                        .layoutId(TextFieldLayoutDefaults.SUPPORTING_ID)
                        .wrapContentHeight()
                        .padding(top = 4.dp)
                        .padding(start = 0.dp, end = 0.dp)
                ) { supporting() }
            }
        },
        measurePolicy = measurePolicy
    )
}

private const val LAYOUT_ID_PREFIX = "Prefix"
private const val LAYOUT_ID_SUFFIX = "Suffix"
