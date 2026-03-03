package org.example.uisamples.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.uisamples.previewproviders.LongAndShortTitlesProvider
import org.example.uisamples.themes.B
import org.example.uisamples.themes.BizonTheme

@Composable
fun BizonAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    type: AppBarType,
    padding: AppBarPadding,
    leadingContent: @Composable (() -> Unit),
    trailingContent: @Composable (() -> Unit)? = null,
    backgroundColor: Color = B.colors.white
) {
    Row(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = padding.horizontalPadding)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent()

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            text = title.orEmpty(),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = B.colors.primary,
            style = type.titleStyle(),
        )

        if (trailingContent != null) {
            trailingContent()
        } else {
            Spacer(modifier = Modifier.width(type.placeholderSize))
        }
    }
}

@Immutable
class AppBarType private constructor(
    val titleStyle: @Composable () -> TextStyle,
    val placeholderSize: Dp,
) {

    companion object {
        val Normal = AppBarType(
            titleStyle = { B.typography.title.large },
            placeholderSize = 16.dp,
        )
    }
}

@Immutable
class AppBarPadding private constructor(
    val horizontalPadding: Dp,
) {

    companion object {
        val Small = AppBarPadding(
            horizontalPadding = 16.dp,
        )
    }
}

@PreviewLightDark
@Composable
private fun BizonAppBarPreview(
    @PreviewParameter(LongAndShortTitlesProvider::class) title: String,
) {
    BizonTheme(isSystemInDarkTheme()) {
        BizonAppBar(
            modifier = Modifier,
            title = title,
            type = AppBarType.Normal,
            padding = AppBarPadding.Small,
            leadingContent = {},
            trailingContent = null,
        )
    }
}
