package org.example.android.uikit.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        false,
        true
    )
}
