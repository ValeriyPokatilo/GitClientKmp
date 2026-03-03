package org.example.uisamples.components.textfields.visualtransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class BlinkingVisualTransformation(
    private val showLastChar: Boolean
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = if (showLastChar && text.isNotEmpty()) {
            val maskedText = "•".repeat(text.length - 1) + text.last()
            AnnotatedString(maskedText)
        } else {
            AnnotatedString("•".repeat(text.length))
        }
        return TransformedText(transformedText, OffsetMapping.Identity)
    }
}
