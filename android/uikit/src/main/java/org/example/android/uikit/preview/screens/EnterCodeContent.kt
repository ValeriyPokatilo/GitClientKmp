package org.example.android.uikit.preview.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.components.ExperimentalComponent
import org.example.android.uikit.components.button.DefaultButton
import org.example.android.uikit.components.button.DefaultButtonStyle
import org.example.android.uikit.components.textField.DefaultTextField
import org.example.android.uikit.components.topAppBar.DefaultTopAppBar
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod")
@ExperimentalComponent("Improve structure, use OtpInputField instead")
@Composable
internal fun EnterCodeContent(
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars,
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.background,
        contentWindowInsets = contentWindowInsets,
        topBar = {
            DefaultTopAppBar(
                title = "Код",
                leading = {
                    DefaultButton(
                        modifier = Modifier
                            .size(44.dp),
                        onClick = {},
                        style = DefaultButtonStyle.Tonal,
                        icon = painterResource(
                            id = R.drawable.ic_left_arrow
                        ),
                    )
                }
            )
        },
        bottomBar = {
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                enabled = true,
                onClick = {},
                style = DefaultButtonStyle.Filled,
                text = "Отправить код",
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    ),
                style = AppTheme.typography.label.medium,
                text = "Отправили вам код на номер телефона",
                color = AppTheme.colors.onSecondaryContainer,
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                style = AppTheme.typography.label.medium,
                text = "+7 (987) 654-32-10",
                color = AppTheme.colors.onSecondaryContainer,
            )
            Row(
                modifier = Modifier
                    .width(304.dp)
                    .padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                DigitInput()
                DigitInput()
                DigitInput()
                DigitInput()
                DigitInput()
                DigitInput()
            }
        }
    }
}

@ExperimentalComponent("Use OtpInputField instead")
@Composable
private fun DigitInput() {
    var textFieldValue: String by remember {
        mutableStateOf("0")
    }
    DefaultTextField(
        modifier = Modifier
            .width(32.dp),
        value = textFieldValue,
        onValueChange = { newText ->
            if (newText.length < 2) {
                textFieldValue = newText
            }
        },
    )
}

@PreviewLightDark
@Composable
private fun EnterCodeContentPreview() = PreviewBody {
    EnterCodeContent()
}
