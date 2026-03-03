package org.example.uisamples.components.inputfield.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.LayoutIdParentData
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirstOrNull
import kotlin.math.max
import kotlin.math.roundToInt

@Suppress("Wrapping")
internal class TextFieldMeasurePolicy(
    private val animationProgress: Float,
    private val paddingValues: PaddingValues
) : MeasurePolicy {

    @Suppress("LongMethod")
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val topPaddingValue: Int = paddingValues.calculateTopPadding().roundToPx()
        val bottomPaddingValue: Int = paddingValues.calculateBottomPadding().roundToPx()

        var occupiedSpaceHorizontally = 0
        var occupiedSpaceVertically = 0

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        // leading icon
        val leadingPlaceable = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.LEADING_ID }
            ?.measure(looseConstraints)
        occupiedSpaceHorizontally += widthOrZero(leadingPlaceable)
        occupiedSpaceVertically = max(
            a = occupiedSpaceVertically,
            b = heightOrZero(leadingPlaceable)
        )

        if (leadingPlaceable != null) {
            occupiedSpaceHorizontally += 6.dp.roundToPx()
        }

        // trailing icon
        val trailingPlaceable = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.TRAILING_ID }
            ?.measure(looseConstraints.offset(horizontal = -occupiedSpaceHorizontally))
        occupiedSpaceHorizontally += widthOrZero(trailingPlaceable)
        occupiedSpaceVertically = max(
            a = occupiedSpaceVertically,
            b = heightOrZero(trailingPlaceable)
        )

        if (trailingPlaceable != null) {
            occupiedSpaceHorizontally += 6.dp.roundToPx()
        }

        // label
        val labelConstraints = looseConstraints.offset(
            vertical = -bottomPaddingValue,
            horizontal = 0
        )
        val labelPlaceable = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.LABEL_ID }
            ?.measure(labelConstraints)

        // input field
        val effectiveTopOffset = topPaddingValue + heightOrZero(labelPlaceable)
        val verticalConstraintOffset = -effectiveTopOffset - bottomPaddingValue
        val textFieldConstraints = constraints
            .copy(minHeight = 0)
            .offset(
                vertical = verticalConstraintOffset,
                horizontal = -occupiedSpaceHorizontally
            )
        val textFieldPlaceable = measurables
            .first { it.layoutId == TextFieldLayoutDefaults.TEXT_FIELD_ID }
            .measure(textFieldConstraints)

        // placeholder
        val placeholderConstraints = textFieldConstraints.copy(minWidth = 0)
        val placeholderPlaceable = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.PLACEHOLDER_ID }
            ?.measure(placeholderConstraints)

        occupiedSpaceVertically = max(
            a = occupiedSpaceVertically,
            b = max(
                a = heightOrZero(placeable = textFieldPlaceable),
                b = heightOrZero(placeholderPlaceable)
            ) + effectiveTopOffset + bottomPaddingValue
        )

        val supportingMeasurable = measurables.fastFirstOrNull {
            it.layoutId == TextFieldLayoutDefaults.SUPPORTING_ID
        }
        val supportingConstraints = looseConstraints
            .offset(vertical = 1)
            .copy(minHeight = 0, maxWidth = constraints.maxWidth)
        val supportingPlaceable = supportingMeasurable?.measure(supportingConstraints)

        val width = calculateWidth(
            leadingWidth = widthOrZero(leadingPlaceable),
            trailingWidth = widthOrZero(trailingPlaceable),
            textFieldWidth = textFieldPlaceable.width,
            constraints = constraints,
        )
        val totalHeight = calculateHeight(
            textFieldHeight = textFieldPlaceable.height,
            labelHeight = heightOrZero(labelPlaceable),
            labelMaxHeight = 20.dp.roundToPx(),
            leadingHeight = heightOrZero(leadingPlaceable),
            trailingHeight = heightOrZero(trailingPlaceable),
            placeholderHeight = heightOrZero(placeholderPlaceable),
            supportingHeight = heightOrZero(supportingPlaceable),
            isLabelFocused = animationProgress == 1f,
            constraints = constraints,
            density = density,
            paddingValues = paddingValues,
        )
        val height = totalHeight

        val containerPlaceable = measurables
            .first { it.layoutId == TextFieldLayoutDefaults.CONTAINER_ID }
            .measure(
                Constraints(
                    minWidth = if (width != Constraints.Infinity) width else 0,
                    maxWidth = width,
                    minHeight = 0,
                    maxHeight = height
                )
            )

        return layout(width, totalHeight) {
            placeWithLabel(
                width = width,
                totalHeight = totalHeight,
                textfieldPlaceable = textFieldPlaceable,
                labelPlaceable = labelPlaceable,
                placeholderPlaceable = placeholderPlaceable,
                leadingPlaceable = leadingPlaceable,
                trailingPlaceable = trailingPlaceable,
                containerPlaceable = containerPlaceable,
                supportingPlaceable = supportingPlaceable,
                textPosition = topPaddingValue + (labelPlaceable?.height ?: 0),
                density = density,
            )
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
            intrinsicMeasurable.minIntrinsicWidth(h)
        }
    }

    private val IntrinsicMeasurable.layoutId: Any?
        get() = (parentData as? LayoutIdParentData)?.layoutId

    private fun intrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int
    ): Int {
        val textFieldWidth = intrinsicMeasurer(
            measurables.first { it.layoutId == TextFieldLayoutDefaults.TEXT_FIELD_ID },
            height
        )
        val trailingWidth = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.TRAILING_ID }
            ?.let { intrinsicMeasurer(it, height) } ?: 0
        val prefixWidth = 0
        val suffixWidth = 0
        val leadingWidth = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.LEADING_ID }
            ?.let { intrinsicMeasurer(it, height) } ?: 0
        val placeholderWidth = measurables
            .find { it.layoutId == TextFieldLayoutDefaults.PLACEHOLDER_ID }
            ?.let { intrinsicMeasurer(it, height) } ?: 0

        return calculateWidth(
            leadingWidth = leadingWidth,
            trailingWidth = trailingWidth,
            prefixWidth = prefixWidth,
            suffixWidth = suffixWidth,
            textFieldWidth = textFieldWidth,
            placeholderWidth = placeholderWidth,
            constraints = TextFieldLayoutDefaults.ZeroConstraints
        )
    }
}

private fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
private fun heightOrZero(placeable: Placeable?) = placeable?.height ?: 0

private fun calculateWidth(
    leadingWidth: Int = 0,
    trailingWidth: Int = 0,
    prefixWidth: Int = 0,
    suffixWidth: Int = 0,
    textFieldWidth: Int = 0,
    placeholderWidth: Int = 0,
    constraints: Constraints
): Int {
    val affixTotalWidth = prefixWidth + suffixWidth
    val middleSection = maxOf(
        textFieldWidth + affixTotalWidth,
        placeholderWidth + affixTotalWidth,
    )
    val wrappedWidth = leadingWidth + middleSection + trailingWidth
    return max(wrappedWidth, constraints.minWidth)
}

private fun calculateHeight(
    textFieldHeight: Int = 0,
    labelHeight: Int = 0,
    labelMaxHeight: Int = 0,
    leadingHeight: Int = 0,
    trailingHeight: Int = 0,
    prefixHeight: Int = 0,
    suffixHeight: Int = 0,
    placeholderHeight: Int = 0,
    supportingHeight: Int = 0,
    isLabelFocused: Boolean,
    constraints: Constraints,
    density: Float,
    paddingValues: PaddingValues
): Int {
    val hasLabel = labelHeight > 0
    // Even though the padding is defined by the developer, if there's a label, it only affects the
    // text field in the focused state. Otherwise, we use the default value.
    val verticalPadding = density * if (!hasLabel || isLabelFocused) {
        (paddingValues.calculateTopPadding() + paddingValues.calculateBottomPadding()).value
    } else {
        (16.dp * 2).value
    }

    val middleSectionHeight = if (hasLabel && isLabelFocused) {
        verticalPadding + labelMaxHeight + max(textFieldHeight, placeholderHeight)
    } else {
        verticalPadding + maxOf(labelHeight, textFieldHeight, placeholderHeight)
    }
    return max(
        constraints.minHeight,
        maxOf(
            leadingHeight,
            trailingHeight,
            prefixHeight,
            suffixHeight,
            middleSectionHeight.roundToInt()
        ) + supportingHeight + (8.dp.value * density).roundToInt()
    )
}

private fun Placeable.PlacementScope.placeWithLabel(
    width: Int,
    totalHeight: Int,
    textfieldPlaceable: Placeable,
    labelPlaceable: Placeable?,
    placeholderPlaceable: Placeable?,
    leadingPlaceable: Placeable?,
    trailingPlaceable: Placeable?,
    prefixPlaceable: Placeable? = null,
    suffixPlaceable: Placeable? = null,
    containerPlaceable: Placeable,
    supportingPlaceable: Placeable? = null,
    textPosition: Int,
    density: Float
) {
    // place container
    containerPlaceable.placeRelative(
        x = 0,
        y = labelPlaceable?.height ?: 0,
    )

    // Most elements should be positioned w.r.t the text field's "visual" height, i.e., excluding
    // the supporting text on bottom
    val height = totalHeight - heightOrZero(supportingPlaceable)

    leadingPlaceable?.placeRelative(
        x = 0,
        y = (labelPlaceable?.height ?: 0) + (12.dp.value * density).roundToInt(),
    )
    trailingPlaceable?.placeRelative(
        x = width - trailingPlaceable.width,
        y = (labelPlaceable?.height ?: 0) + (12.dp.value * density).roundToInt(),
    )
    labelPlaceable?.placeRelative(x = 0, y = 0)

    prefixPlaceable?.placeRelative(widthOrZero(leadingPlaceable), textPosition)
    suffixPlaceable?.placeRelative(
        x = width - widthOrZero(trailingPlaceable) - suffixPlaceable.width,
        y = textPosition,
    )

    val leadingPlaceableWidth: Int = leadingPlaceable?.let {
        it.width + (6.dp.value * density).roundToInt()
    } ?: 0

    val textHorizontalPosition: Int = leadingPlaceableWidth + widthOrZero(prefixPlaceable)
    textfieldPlaceable.placeRelative(
        x = textHorizontalPosition,
        y = textPosition
    )

    placeholderPlaceable?.placeRelative(x = textHorizontalPosition, y = textPosition)
    supportingPlaceable?.placeRelative(x = 0, y = height)
}
