package org.example.android.uikit.components.checkBox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.android.uikit.components.text.createStringWithLinks
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun LinkedTextCheckbox(
    text: String,
    clickableSpans: List<String>,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLinkedTextClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    checkboxColors: CheckboxColors = AppTheme.componentColors.checkboxColors,
    textStyle: TextStyle = AppTheme.typography.body.large,
    linkStyle: SpanStyle? = null,
) {
    Row(
        modifier = modifier
    ) {
        BaseCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = checkboxColors,
        )

        val annotatedText = remember(text, clickableSpans, linkStyle, onLinkedTextClick) {
            createStringWithLinks(
                text = text,
                clickableSpans = clickableSpans,
                onLinkedTextClick = onLinkedTextClick,
                linkStyle = linkStyle,
            )
        }

        Text(
            modifier = Modifier
                // add padding to align with BaseCheckbox since BaseCheckbox has inner paddings
                .padding(top = 16.dp, end = 12.dp, bottom = 12.dp),
            text = annotatedText,
            style = textStyle
        )
    }
}

@PreviewLightDark
@Composable
private fun LinkedTextCheckboxPreview() = PreviewBody {
    val linkText = "политикой хранения персональных данных"

    LinkedTextCheckbox(
        text = "Я согласен с $linkText",
        clickableSpans = listOf(linkText),
        checked = true,
        linkStyle = SpanStyle(
            color = AppTheme.colors.link,
            textDecoration = TextDecoration.Underline,
        ),
        onCheckedChange = {},
        onLinkedTextClick = {},
    )
}
