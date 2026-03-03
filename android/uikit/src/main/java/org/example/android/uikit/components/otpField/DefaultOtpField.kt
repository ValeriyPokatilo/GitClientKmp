package org.example.android.uikit.components.otpField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultOtpField(
    length: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: String? = null,
    isError: Boolean = error != null,
    colors: OtpFieldColors = AppTheme.componentColors.otpFieldColors,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = modifier
    ) {
        BaseOtpField(
            modifier = Modifier.focusRequester(focusRequester),
            length = length,
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            isError = isError,
            colors = colors,
        )

        error?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = it,
                textAlign = TextAlign.Center,
                color = colors.errorSupportingTextColor,
                style = AppTheme.typography.body.large
            )
        }
    }
}

/**
 * All available colors for OtpField:
 * textColor - default, error
 * containerColor - default, active, error
 * indicatorColor - default, active, error
 * supportingTextColor - error
 * cursorColor - default
 */
@Composable
fun defaultOtpFieldColors(): OtpFieldColors = OtpFieldColors(
    defaultTextColor = AppTheme.colors.onSurface,
    errorTextColor = AppTheme.colors.error,
    defaultContainerColor = AppTheme.colors.transparent,
    activeContainerColor = AppTheme.colors.transparent,
    errorContainerColor = AppTheme.colors.transparent,
    defaultIndicatorColor = AppTheme.colors.outline,
    activeIndicatorColor = AppTheme.colors.primary,
    errorIndicatorColor = AppTheme.colors.error,
    errorSupportingTextColor = AppTheme.colors.error,
    cursorColor = AppTheme.colors.onSurface,
)

@MultiPreview
@Composable
private fun DefaultOtpFieldPreview() = PreviewColumn(spacedBy = 24.dp) {
    DefaultOtpField(
        modifier = Modifier.fillMaxWidth(),
        length = 5,
        value = "",
        onValueChange = { },
        error = null,
    )

    DefaultOtpField(
        modifier = Modifier.fillMaxWidth(),
        length = 5,
        value = "12",
        onValueChange = { },
        error = null,
    )

    DefaultOtpField(
        modifier = Modifier.fillMaxWidth(),
        length = 5,
        value = "12345",
        onValueChange = { },
        error = null,
    )

    DefaultOtpField(
        modifier = Modifier.fillMaxWidth(),
        length = 5,
        value = "12345",
        onValueChange = { },
        error = "Неправильный код",
    )
}
