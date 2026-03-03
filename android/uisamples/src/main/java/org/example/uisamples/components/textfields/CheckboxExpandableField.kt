package org.example.uisamples.components.textfields

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.example.android.uisamples.R
import org.example.uisamples.themes.GgcTheme

@Composable
fun CheckboxExpandableField(
    modifier: Modifier = Modifier,
    text: MutableState<String>,
    checkboxLabel: String,
    label: String,
    enabled: Boolean = true,
    error: String? = null
) {
    val focusRequester: FocusRequester = remember { FocusRequester() }
    var isFocused: Boolean by remember { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        if (isFocused || text.value.isNotBlank()) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            LabeledSingleLineTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                text = text,
                label = label,
                error = error,
                enabled = enabled,
            )
        } else {
            Row(
                modifier = Modifier
                    .alpha(alpha = if (enabled) 1f else .3f)
                    .clickable {
                        isFocused = true
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    painter = painterResource(R.drawable.ic_checkbox_unchecked),
                    contentDescription = null
                )
                Text(
                    text = checkboxLabel,
                    style = GgcTheme.typography.descriptionMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
