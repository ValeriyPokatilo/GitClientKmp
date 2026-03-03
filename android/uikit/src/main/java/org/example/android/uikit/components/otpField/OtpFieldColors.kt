package org.example.android.uikit.components.otpField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

data class OtpFieldColors(
    val defaultTextColor: Color = Color.Unspecified,
    val errorTextColor: Color = Color.Unspecified,
    val defaultContainerColor: Color = Color.Unspecified,
    val activeContainerColor: Color = Color.Unspecified,
    val errorContainerColor: Color = Color.Unspecified,
    val defaultIndicatorColor: Color = Color.Unspecified,
    val activeIndicatorColor: Color = Color.Unspecified,
    val errorIndicatorColor: Color = Color.Unspecified,
    val errorSupportingTextColor: Color = Color.Unspecified,
    val cursorColor: Color = Color.Unspecified,
) {
    /**
     * Represents the color used for the leading icon of this otp field.
     *
     * @param isError whether the text field's current value is in error
     */
    @Composable
    fun textColor(isError: Boolean): Color {
        return when {
            isError -> errorTextColor
            else -> defaultTextColor
        }
    }

    /**
     * Represents the container color for this otp field's boxes.
     *
     * @param isActive whether the otp field's current box is active
     * @param isError whether the otp field's current box is in error
     */
    @Stable
    internal fun containerColor(isActive: Boolean, isError: Boolean): Color {
        return when {
            isActive -> activeContainerColor
            isError -> errorContainerColor
            else -> defaultContainerColor
        }
    }

    /**
     * Represents the color used for the border indicator of this otp field's boxes.
     *
     * @param isActive whether the otp field's current box is active
     * @param isError whether the otp field's current box is in error
     */
    @Composable
    fun indicatorColor(isActive: Boolean, isError: Boolean): Color {
        return when {
            isActive -> activeIndicatorColor
            isError -> errorIndicatorColor
            else -> defaultIndicatorColor
        }
    }
}
