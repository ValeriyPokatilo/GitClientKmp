package org.example.android.uikit.components.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString

fun createStringWithLinks(
    text: String,
    clickableSpans: List<String>,
    onLinkedTextClick: (index: Int) -> Unit,
    linkStyle: SpanStyle? = null,
): AnnotatedString = buildAnnotatedString {
    append(text)
    clickableSpans.forEachIndexed { index, span ->
        val startIndex = text.indexOf(
            string = span,
            ignoreCase = true
        )
        if (startIndex != -1) {
            addLink(
                url = LinkAnnotation.Url(
                    url = "",
                    styles = TextLinkStyles(
                        style = linkStyle
                    ),
                    linkInteractionListener = {
                        onLinkedTextClick(index)
                    }
                ),
                start = startIndex,
                end = startIndex + span.length
            )
        }
    }
}
