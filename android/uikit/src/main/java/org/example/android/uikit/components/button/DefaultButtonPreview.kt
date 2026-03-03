package org.example.android.uikit.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview500
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.preview.previewInteractionSource

@Composable
private fun ButtonsGroupPreview(
    style: DefaultButtonStyle,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    DefaultButton(
        enabled = enabled,
        onClick = {},
        style = style,
        icon = null,
        text = "Text label",
        interactionSource = interactionSource,
    )
    DefaultButton(
        enabled = enabled,
        onClick = {},
        style = style,
        icon = painterResource(id = R.drawable.ic_play),
        text = "Text label",
        interactionSource = interactionSource,
    )
    DefaultButton(
        enabled = enabled,
        onClick = {},
        style = style,
        icon = painterResource(id = R.drawable.ic_play),
        text = null,
        interactionSource = interactionSource,
    )
}

@Composable
private fun ButtonsColumnPreview(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    Column {
        ButtonsGroupPreview(
            enabled = enabled,
            style = DefaultButtonStyle.Filled,
            interactionSource = interactionSource,
        )
        ButtonsGroupPreview(
            enabled = enabled,
            style = DefaultButtonStyle.Tonal,
            interactionSource = interactionSource,
        )
        ButtonsGroupPreview(
            enabled = enabled,
            style = DefaultButtonStyle.Outlined,
            interactionSource = interactionSource,
        )
        ButtonsGroupPreview(
            enabled = enabled,
            style = DefaultButtonStyle.Simple,
            interactionSource = interactionSource,
        )
    }
}

@MultiPreview500
@Composable
private fun DefaultButtonsStatesPreview() = PreviewBody {
    Row {
        ButtonsColumnPreview(
            enabled = true,
            interactionSource = previewInteractionSource()
        )
        Spacer(modifier = Modifier.requiredWidth(32.dp))
        ButtonsColumnPreview(
            enabled = true,
            interactionSource = previewInteractionSource(isPressed = true)
        )
        Spacer(modifier = Modifier.requiredWidth(32.dp))
        ButtonsColumnPreview(
            enabled = false,
            interactionSource = previewInteractionSource()
        )
    }
}
