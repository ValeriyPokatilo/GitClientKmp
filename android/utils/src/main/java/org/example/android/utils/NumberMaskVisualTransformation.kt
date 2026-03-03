package org.example.android.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

open class NumberMaskVisualTransformation(
    private val mask: String
) : VisualTransformation {
    private val maskNumber: Char = MASK_CHAR

    override fun filter(text: AnnotatedString): TransformedText {
        val maxLength = mask.count { it == maskNumber }
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskNumber) {
                    val nextDigitIndex = mask.indexOf(maskNumber, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return if (text.isEmpty()) {
            TransformedText(text, NumberMaskOffsetMapper(mask, maskNumber))
        } else {
            TransformedText(annotatedString, NumberMaskOffsetMapper(mask, maskNumber))
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other !is NumberMaskVisualTransformation -> false
            this::class != other::class -> false
            else -> this.mask == other.mask
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    private companion object {
        private const val MASK_CHAR = '#'
    }
}

private class NumberMaskOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        if (offset == 0) {
            return 0
        }
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount) {
            val maskCharIndex = i++
            if (mask.length > maskCharIndex) {
                if (mask[maskCharIndex] != numberChar) noneDigitCount++
            }
        }
        return offset + noneDigitCount
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}

class PhoneVisualTransformation : NumberMaskVisualTransformation(PHONE_MASK_RU)

class DateVisualTransformation : NumberMaskVisualTransformation(DATE_MASK_RU)

private const val PHONE_MASK_RU = "+7 (###) ###-##-##"
private const val DATE_MASK_RU = "##.##.####"
