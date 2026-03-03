package org.example.android.uikit.components.otpField

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.android.uikit.theme.AppTheme

@Composable
fun BaseOtpField(
    length: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    colors: OtpFieldColors = OtpFieldColors(),
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= length && enabled) {
                onValueChange(newValue)
            }
        },
        textStyle = TextStyle(color = Color.Transparent),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier.widthIn(max = 64.dp * length - 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(length) { index ->
                        val char: String = when {
                            index >= value.length -> ""
                            else -> value[index].toString()
                        }
                        val isActive: Boolean = index == value.length

                        OtpInputBox(
                            modifier = Modifier.weight(1f),
                            char = char,
                            isActive = isActive,
                            isError = isError,
                            colors = colors,
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun OtpInputBox(
    modifier: Modifier = Modifier,
    char: String,
    isError: Boolean,
    isActive: Boolean,
    colors: OtpFieldColors,
) {
    Box(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = colors.indicatorColor(isActive = isActive, isError = isError),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = AppTheme.typography.body.large,
            color = colors.textColor(isError = isError),
        )

        if (isActive) {
            OtpInputCursor(
                color = colors.cursorColor,
            )
        }
    }
}

@Composable
private fun OtpInputCursor(
    color: Color,
) {
    val cursorTransition = rememberInfiniteTransition(label = "cursorTransition")

    val alphaValue by cursorTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorTransition"
    )

    Box(
        modifier = Modifier
            .height(24.dp)
            .width(1.dp)
            .alpha(alphaValue)
            .background(color)
    )
}
