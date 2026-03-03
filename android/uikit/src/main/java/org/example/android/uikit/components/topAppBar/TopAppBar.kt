package org.example.android.uikit.components.topAppBar

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.components.button.DefaultButton
import org.example.android.uikit.components.button.DefaultButtonStyle
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    horizontalPadding: Dp = 8.dp,
    containerColor: Color = AppTheme.colors.background,
    windowInsets: WindowInsets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    isContentCentred: Boolean = false,
    leading: @Composable (BoxScope.() -> Unit)? = null,
    trailing: @Composable (BoxScope.() -> Unit)? = null,
) = BaseTopAppBar(
    modifier = modifier,
    height = height,
    horizontalPadding = horizontalPadding,
    containerColor = containerColor,
    windowInsets = windowInsets,
    isContentCentered = isContentCentred,
    leading = leading,
    trailing = trailing,
    content = {
        TopAppBarTitle(text = title)
    }
)

@Composable
fun BackTopAppBar(
    title: String,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    horizontalPadding: Dp = 8.dp,
    containerColor: Color = AppTheme.colors.background,
    windowInsets: WindowInsets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    trailing: @Composable (BoxScope.() -> Unit)? = null,
) {
    BaseTopAppBar(
        modifier = modifier,
        height = height,
        horizontalPadding = horizontalPadding,
        containerColor = containerColor,
        windowInsets = windowInsets,
        leading = {
            DefaultButton(
                modifier = Modifier.size(44.dp),
                onClick = onClickBack,
                style = DefaultButtonStyle.Simple,
                icon = painterResource(R.drawable.ic_left_arrow),
            )
        },
        trailing = trailing,
        content = {
            TopAppBarTitle(text = title)
        }
    )
}

@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    horizontalPadding: Dp = 8.dp,
    containerColor: Color = AppTheme.colors.background,
    windowInsets: WindowInsets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    isContentCentred: Boolean = false,
    leading: @Composable (BoxScope.() -> Unit)? = null,
    trailing: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    BaseTopAppBar(
        modifier = modifier,
        height = height,
        horizontalPadding = horizontalPadding,
        containerColor = containerColor,
        windowInsets = windowInsets,
        isContentCentered = isContentCentred,
        leading = leading,
        trailing = trailing,
        content = content
    )
}

@PreviewLightDark
@Composable
private fun TopAppBarPreview() = PreviewBody {
    DefaultTopAppBar(
        title = "Title очень длинный вариант для тестирования",
        trailing = {
            Row {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = { },
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_plus),
                )
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = { },
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_profile),
                )
            }
        },
    )
}
