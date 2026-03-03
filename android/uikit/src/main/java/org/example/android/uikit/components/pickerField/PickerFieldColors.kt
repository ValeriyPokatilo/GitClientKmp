package org.example.android.uikit.components.pickerField

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
data class PickerFieldColors(
    val focusedTextColor: Color = Color.Unspecified,
    val unfocusedTextColor: Color = Color.Unspecified,
    val disabledTextColor: Color = Color.Unspecified,
    val errorTextColor: Color = Color.Unspecified,
    val validTextColor: Color = Color.Unspecified,
    val focusedContainerColor: Color = Color.Unspecified,
    val unfocusedContainerColor: Color = Color.Unspecified,
    val disabledContainerColor: Color = Color.Unspecified,
    val errorContainerColor: Color = Color.Unspecified,
    val validContainerColor: Color = Color.Unspecified,
    val focusedIndicatorColor: Color = Color.Unspecified,
    val unfocusedIndicatorColor: Color = Color.Unspecified,
    val disabledIndicatorColor: Color = Color.Unspecified,
    val errorIndicatorColor: Color = Color.Unspecified,
    val validIndicatorColor: Color = Color.Unspecified,
    val focusedLeadingIconColor: Color = Color.Unspecified,
    val unfocusedLeadingIconColor: Color = Color.Unspecified,
    val disabledLeadingIconColor: Color = Color.Unspecified,
    val errorLeadingIconColor: Color = Color.Unspecified,
    val focusedTrailingIconColor: Color = Color.Unspecified,
    val unfocusedTrailingIconColor: Color = Color.Unspecified,
    val disabledTrailingIconColor: Color = Color.Unspecified,
    val errorTrailingIconColor: Color = Color.Unspecified,
    val focusedLabelColor: Color = Color.Unspecified,
    val unfocusedLabelColor: Color = Color.Unspecified,
    val disabledLabelColor: Color = Color.Unspecified,
    val errorLabelColor: Color = Color.Unspecified,
    val focusedPlaceholderColor: Color = Color.Unspecified,
    val unfocusedPlaceholderColor: Color = Color.Unspecified,
    val disabledPlaceholderColor: Color = Color.Unspecified,
    val errorPlaceholderColor: Color = Color.Unspecified,
    val errorSupportingTextColor: Color = Color.Unspecified,
) {

    /**
     * Represents the color used for the leading icon of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun leadingIconColor(
        enabled: Boolean,
        isError: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledLeadingIconColor
        isError -> errorLeadingIconColor
        focused -> focusedLeadingIconColor
        else -> unfocusedLeadingIconColor
    }

    /**
     * Represents the color used for the trailing icon of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun trailingIconColor(
        enabled: Boolean,
        isError: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledTrailingIconColor
        isError -> errorTrailingIconColor
        focused -> focusedTrailingIconColor
        else -> unfocusedTrailingIconColor
    }

    /**
     * Represents the color used for the border indicator of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun indicatorColor(
        enabled: Boolean,
        isError: Boolean,
        isValid: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledIndicatorColor
        isError -> errorIndicatorColor
        isValid -> validIndicatorColor
        focused -> focusedIndicatorColor
        else -> unfocusedIndicatorColor
    }

    /**
     * Represents the container color for this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun containerColor(
        enabled: Boolean,
        isError: Boolean,
        isValid: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledContainerColor
        isError -> errorContainerColor
        isValid -> validContainerColor
        focused -> focusedContainerColor
        else -> unfocusedContainerColor
    }

    /**
     * Represents the color used for the placeholder of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun placeholderColor(
        enabled: Boolean,
        isError: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledPlaceholderColor
        isError -> errorPlaceholderColor
        focused -> focusedPlaceholderColor
        else -> unfocusedPlaceholderColor
    }

    /**
     * Represents the color used for the label of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun labelColor(
        enabled: Boolean,
        isError: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledLabelColor
        isError -> errorLabelColor
        focused -> focusedLabelColor
        else -> unfocusedLabelColor
    }

    /**
     * Represents the color used for the input field of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param isError whether the text field's current value is in error
     * @param focused whether the text field is in focus
     */
    @Stable
    internal fun textColor(
        enabled: Boolean,
        isError: Boolean,
        isValid: Boolean,
        focused: Boolean,
    ): Color = when {
        !enabled -> disabledTextColor
        isError -> errorTextColor
        isValid -> validTextColor
        focused -> focusedTextColor
        else -> unfocusedTextColor
    }
}
