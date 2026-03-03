package org.example.android.uikit.theme

import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import org.example.android.uikit.components.checkBox.defaultCheckboxColors
import org.example.android.uikit.components.dropDownMenu.DropDownMenuColors
import org.example.android.uikit.components.dropDownMenu.defaultDropDownMenuColors
import org.example.android.uikit.components.navigationBar.NavigationBarColors
import org.example.android.uikit.components.navigationBar.defaultNavigationBarColors
import org.example.android.uikit.components.otpField.OtpFieldColors
import org.example.android.uikit.components.otpField.defaultOtpFieldColors
import org.example.android.uikit.components.pickerField.PickerFieldColors
import org.example.android.uikit.components.pickerField.defaultPickerFieldColors
import org.example.android.uikit.components.switches.defaultSwitchColors
import org.example.android.uikit.components.textField.defaultTextFieldColors
import org.example.android.uikit.components.typeTabs.TypeTabsColors
import org.example.android.uikit.components.typeTabs.defaultTypeTabsColors

@Immutable
data class ComponentColors(
    val switchColors: SwitchColors,
    val checkboxColors: CheckboxColors,
    val typeTabsColors: TypeTabsColors,
    val otpFieldColors: OtpFieldColors,
    val textFieldColors: TextFieldColors,
    val pickerFieldColors: PickerFieldColors,
    val dropDownMenuColors: DropDownMenuColors,
    val navigationBarColors: NavigationBarColors,
)

@Composable
internal fun componentColors(): ComponentColors {
    return ComponentColors(
        switchColors = defaultSwitchColors(),
        checkboxColors = defaultCheckboxColors(),
        typeTabsColors = defaultTypeTabsColors(),
        otpFieldColors = defaultOtpFieldColors(),
        textFieldColors = defaultTextFieldColors(),
        pickerFieldColors = defaultPickerFieldColors(),
        dropDownMenuColors = defaultDropDownMenuColors(),
        navigationBarColors = defaultNavigationBarColors(),
    )
}

internal val LocalComponentColors: ProvidableCompositionLocal<ComponentColors> =
    staticCompositionLocalOf {
        error("LocalComponentColors not initialized yet")
    }
