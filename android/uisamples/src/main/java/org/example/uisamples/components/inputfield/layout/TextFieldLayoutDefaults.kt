package org.example.uisamples.components.inputfield.layout

import androidx.compose.ui.unit.Constraints

internal object TextFieldLayoutDefaults {

    const val ANIMATION_DURATION = 150

    const val TEXT_FIELD_ID = "TextField"
    const val LABEL_ID = "Label"
    const val LEADING_ID = "Leading"
    const val TRAILING_ID = "Trailing"
    const val PLACEHOLDER_ID = "Placeholder"
    const val CONTAINER_ID = "Container"
    const val SUPPORTING_ID = "Supporting"

    val ZeroConstraints = Constraints(
        minWidth = 0,
        maxWidth = 0,
        minHeight = 0,
        maxHeight = 0
    )
}
