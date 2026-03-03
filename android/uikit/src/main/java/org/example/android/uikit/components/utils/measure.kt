package org.example.android.uikit.components.utils

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull

internal fun List<Measurable>.measure(layoutId: String, constraints: Constraints): Placeable {
    return this.fastFirst { it.layoutId == layoutId }
        .measure(constraints)
}

internal fun List<Measurable>.measureOrNull(layoutId: String, constraints: Constraints): Placeable? {
    return this.fastFirstOrNull { it.layoutId == layoutId }
        ?.measure(constraints)
}
