package org.example.android.uikit.components.topAppBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.components.button.DefaultButton
import org.example.android.uikit.components.button.DefaultButtonStyle
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod")
@PreviewLightDark
@Composable
private fun TopAppBarPreview() = PreviewBody {
    Column(
        modifier = Modifier
            .background(color = AppTheme.colors.primaryContainer)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        DefaultTopAppBar(
            title = "Title",
            leading = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = {},
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_burger_menu),
                )
            },
            trailing = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = { },
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_profile),
                )
            },
        )

        DefaultTopAppBar(
            title = "Title",
            leading = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = {},
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_burger_menu),
                )
            },
            trailing = {
                Row {
                    DefaultButton(
                        modifier = Modifier
                            .size(44.dp),
                        onClick = { },
                        style = DefaultButtonStyle.Simple,
                        icon = painterResource(
                            id = R.drawable.ic_plus
                        ),
                    )
                    DefaultButton(
                        modifier = Modifier
                            .size(44.dp),
                        onClick = { },
                        style = DefaultButtonStyle.Simple,
                        icon = painterResource(
                            id = R.drawable.ic_profile,
                        )
                    )
                }
            },
        )

        DefaultTopAppBar(
            title = "Title",
            leading = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = {},
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_left_arrow),
                )
            },
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
                        icon = painterResource(R.drawable.ic_plus),
                    )
                    DefaultButton(
                        modifier = Modifier
                            .size(44.dp),
                        onClick = { },
                        style = DefaultButtonStyle.Simple,
                        icon = painterResource(R.drawable.ic_profile)
                    )
                }
            },
        )

        DefaultTopAppBar(
            title = "Title",
            leading = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = {},
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_left_arrow),
                )
            },
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

        DefaultTopAppBar(
            title = "Title",
            leading = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = {},
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_left_arrow),
                )
            },
            trailing = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = { },
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_profile),
                )
            },
        )

        DefaultTopAppBar(
            title = "Title",
            leading = {
                DefaultButton(
                    modifier = Modifier.size(44.dp),
                    onClick = {},
                    style = DefaultButtonStyle.Simple,
                    icon = painterResource(R.drawable.ic_left_arrow),
                )
            },
        )
    }
}
