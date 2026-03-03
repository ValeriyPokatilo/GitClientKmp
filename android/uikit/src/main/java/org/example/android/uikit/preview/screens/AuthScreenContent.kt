package org.example.android.uikit.preview.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.components.button.DefaultButton
import org.example.android.uikit.components.button.DefaultButtonStyle
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod")
@Composable
private fun AuthScreenContent() {
    Column(
        modifier = Modifier
            .width(320.dp)
            .background(color = AppTheme.colors.background)
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 40.dp,
                    minHeight = 288.dp
                )
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            DefaultButton(
                icon = painterResource(id = R.drawable.ic_play),
                style = DefaultButtonStyle.Tonal,
                enabled = true,
                onClick = {},
            )
        }
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = "Вход",
            style = DefaultButtonStyle.Filled,
            enabled = true,
            onClick = {},
        )
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = "Регистрация",
            style = DefaultButtonStyle.Outlined,
            enabled = true,
            onClick = {},
        )
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            text = "Вход через ICEROCK.DEV",
            style = DefaultButtonStyle.Outlined,
            enabled = false,
            onClick = {},
        )
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = "Сменить тему на темную",
            icon = painterResource(id = R.drawable.ic_play),
            style = DefaultButtonStyle.Simple,
            enabled = true,
            onClick = {},
        )
    }
}

@MultiPreview
@Composable
private fun AuthScreenContentPreview() = PreviewBody {
    AuthScreenContent()
}
