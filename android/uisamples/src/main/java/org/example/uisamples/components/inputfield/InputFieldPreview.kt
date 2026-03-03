package org.example.uisamples.components.inputfield

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.android.uisamples.R

@Suppress("MagicNumber", "LongMethod")
@Composable
private fun InputFieldPreviewRow(
    initFieldValues: List<String> = listOf(
        "",
        "Value",
        "Value",
        ""
    ),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
) {
    var textFieldValue: String by remember {
        mutableStateOf(initFieldValues[0])
    }
    var textFieldValue2: String by remember {
        mutableStateOf(initFieldValues[1])
    }
    var textFieldValue3: String by remember {
        mutableStateOf(initFieldValues[2])
    }
    var textFieldValue4: String by remember {
        mutableStateOf(initFieldValues[3])
    }
    Row {
        InputField(
            modifier = Modifier
                .width(240.dp)
                .padding(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 8.dp
                ),
            value = textFieldValue,
            onValueChange = { newText ->
                textFieldValue = newText
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            label = "Label",
            placeholder = "Placeholder",
            description = null,
            error = error,
        )
        InputField(
            modifier = Modifier
                .width(240.dp)
                .padding(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 8.dp
                ),
            value = textFieldValue2,
            onValueChange = { newText ->
                textFieldValue2 = newText
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            label = "Label",
            placeholder = "Placeholder",
            description = null,
            error = error,
        )
        InputField(
            modifier = Modifier
                .width(240.dp)
                .padding(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 8.dp
                ),
            value = textFieldValue3,
            onValueChange = { newText ->
                textFieldValue3 = newText
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            label = "Label",
            placeholder = "Placeholder",
            description = null,
            error = error,
        )
        InputField(
            modifier = Modifier
                .width(240.dp)
                .padding(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 8.dp
                ),
            value = textFieldValue4,
            onValueChange = { newText ->
                textFieldValue4 = newText
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            label = "Label",
            placeholder = "Placeholder",
            description = null,
            error = error,
            enabled = false,
        )
    }
}

@Preview(
    name = "appLightColors",
    group = "theme",
    showBackground = true,
    showSystemUi = false,
    widthDp = 960,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "appDarkColors",
    group = "theme",
    showBackground = true,
    showSystemUi = false,
    widthDp = 960,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun InputFieldPreview() {
    Column {
        InputFieldPreviewRow(
            initFieldValues = listOf(
                "",
                "Value",
                "Value",
                ""
            ),
        )
        InputFieldPreviewRow(
            initFieldValues = listOf(
                "+7 (000) 000-00-00",
                "+7 (987) 654-32-10",
                "+7 (987) 654-32-10",
                "+7 (000) 000-00-00"
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(R.drawable.ic_phone),
                    contentDescription = null
                )
            }
        )
        InputFieldPreviewRow(
            initFieldValues = listOf(
                "",
                "Value",
                "Value",
                ""
            ),
            error = "Error text"
        )
    }
}
