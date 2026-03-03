package org.example.android.uikit.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.android.uikit.theme.AppTheme

@Composable
private fun ColorCard(
    color: Color,
    textColor: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            modifier = Modifier,
            text = text,
            color = textColor,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        )
    }
}

@Composable
private fun ColorCardPair(
    color: Color,
    onColor: Color,
    text: String,
) {
    ColorCard(
        modifier = Modifier
            .size(276.dp, 81.dp)
            .padding(bottom = 4.dp),
        color = color,
        textColor = onColor,
        text = text,
    )
    ColorCard(
        modifier = Modifier
            .size(276.dp, 40.dp),
        color = onColor,
        textColor = color,
        text = "On $text",
    )
}

@Composable
private fun ColorCardTriple(
    color: Color,
    colorFocus: Color,
    onColor: Color,
    text: String,
) {
    ColorCard(
        modifier = Modifier
            .size(276.dp, 38.5.dp)
            .padding(bottom = 4.dp),
        color = color,
        textColor = onColor,
        text = text,
    )
    ColorCard(
        modifier = Modifier
            .size(276.dp, 38.5.dp)
            .padding(bottom = 4.dp),
        color = colorFocus,
        textColor = onColor,
        text = "$text-focus",
    )
    ColorCard(
        modifier = Modifier
            .size(276.dp, 40.dp),
        color = onColor,
        textColor = color,
        text = "On $text",
    )
}

@Composable
private fun ColorCardGroup(
    color: Color,
    onColor: Color,
    containerColor: Color,
    onContainerColor: Color,
    text: String,
) {
    Column {
        ColorCardPair(
            color = color,
            onColor = onColor,
            text = text,
        )
        Spacer(modifier = Modifier.requiredHeight(8.dp))
        ColorCardPair(
            color = containerColor,
            onColor = onContainerColor,
            text = "$text Container",
        )
    }
}

@Composable
private fun ColorCardTripleGroup(
    color: Color,
    colorFocus: Color,
    onColor: Color,
    containerColor: Color,
    onContainerColor: Color,
    text: String,
) {
    Column {
        ColorCardTriple(
            color = color,
            colorFocus = colorFocus,
            onColor = onColor,
            text = text,
        )
        Spacer(modifier = Modifier.requiredHeight(7.dp))
        ColorCardPair(
            color = containerColor,
            onColor = onContainerColor,
            text = "$text Container",
        )
    }
}

@Suppress("LongMethod")
@Preview(
    name = "Light",
    group = "theme",
    widthDp = 1460,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    group = "theme",
    widthDp = 1460,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AppColorsPreview() = PreviewBody {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Row {
            ColorCardTripleGroup(
                color = AppTheme.colors.primary,
                colorFocus = AppTheme.colors.primaryFocus,
                onColor = AppTheme.colors.onPrimary,
                containerColor = AppTheme.colors.primaryContainer,
                onContainerColor = AppTheme.colors.onPrimaryContainer,
                text = "Primary",
            )
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            ColorCardTripleGroup(
                color = AppTheme.colors.secondary,
                colorFocus = AppTheme.colors.secondaryFocus,
                onColor = AppTheme.colors.onSecondary,
                containerColor = AppTheme.colors.secondaryContainer,
                onContainerColor = AppTheme.colors.onSecondaryContainer,
                text = "Secondary",
            )
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            ColorCardGroup(
                color = AppTheme.colors.error,
                onColor = AppTheme.colors.onError,
                containerColor = AppTheme.colors.errorContainer,
                onContainerColor = AppTheme.colors.onErrorContainer,
                text = "Error",
            )
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            ColorCardGroup(
                color = AppTheme.colors.valid,
                onColor = AppTheme.colors.onValid,
                containerColor = AppTheme.colors.validContainer,
                onContainerColor = AppTheme.colors.onValidContainer,
                text = "Valid",
            )
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            ColorCardGroup(
                color = AppTheme.colors.warning,
                onColor = AppTheme.colors.onWarning,
                containerColor = AppTheme.colors.warningContainer,
                onContainerColor = AppTheme.colors.onWarningContainer,
                text = "Warning",
            )
        }
        Spacer(modifier = Modifier.requiredHeight(32.dp))
        Row {
            Column {
                ColorCard(
                    modifier = Modifier
                        .size(184.dp, 50.dp)
                        .padding(bottom = 4.dp),
                    color = AppTheme.colors.surface,
                    textColor = AppTheme.colors.onSurface,
                    text = "Surface",
                )
                ColorCard(
                    modifier = Modifier
                        .size(184.dp, 50.dp)
                        .padding(bottom = 4.dp),
                    color = AppTheme.colors.surfaceVariant,
                    textColor = AppTheme.colors.onSurfaceVariant,
                    text = "Surface variant",
                )
            }
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 100.dp)
                    .padding(bottom = 4.dp),
                color = AppTheme.colors.surfaceContainerLowest,
                textColor = AppTheme.colors.onSurface,
                text = "Surf. Container Lowest",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 100.dp)
                    .padding(bottom = 4.dp),
                color = AppTheme.colors.surfaceContainerLow,
                textColor = AppTheme.colors.onSurface,
                text = "Surf. Container Low",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 100.dp)
                    .padding(bottom = 4.dp),
                color = AppTheme.colors.surfaceContainer,
                textColor = AppTheme.colors.onSurface,
                text = "Surf. Container",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 100.dp)
                    .padding(bottom = 4.dp),
                color = AppTheme.colors.surfaceContainerHigh,
                textColor = AppTheme.colors.onSurface,
                text = "Surf. Container High",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 100.dp)
                    .padding(bottom = 4.dp),
                color = AppTheme.colors.surfaceContainerHighest,
                textColor = AppTheme.colors.onSurface,
                text = "Surf. Container Highest",
            )
        }
        Row {
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 40.dp),
                color = AppTheme.colors.onSurface,
                textColor = AppTheme.colors.surface,
                text = "On Surface",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(184.dp, 40.dp),
                color = AppTheme.colors.onSurfaceVariant,
                textColor = AppTheme.colors.surfaceVariant,
                text = "On Surface variant",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(372.dp, 40.dp),
                color = AppTheme.colors.inverseSurface,
                textColor = AppTheme.colors.inverseOnSurface,
                text = "Inverse Surface",
            )
            Spacer(modifier = Modifier.requiredWidth(4.dp))
            ColorCard(
                modifier = Modifier
                    .size(372.dp, 40.dp),
                color = AppTheme.colors.inverseOnSurface,
                textColor = AppTheme.colors.inverseSurface,
                text = "Inverse On Surface",
            )
        }
    }
}
