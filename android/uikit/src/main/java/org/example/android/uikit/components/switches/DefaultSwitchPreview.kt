package org.example.android.uikit.components.switches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody

@Composable
private fun SwitchPreviewColumn(enabled: Boolean = true) {
    Column {
        DefaultSwitch(
            checked = true,
            onCheckedChange = {},
            enabled = enabled,
        )
        DefaultSwitch(
            checked = true,
            onCheckedChange = {},
            icon = painterResource(id = R.drawable.ic_check),
            enabled = enabled,
        )
        DefaultSwitch(
            checked = false,
            onCheckedChange = {},
            enabled = enabled,
        )
        DefaultSwitch(
            checked = false,
            onCheckedChange = {},
            icon = painterResource(id = R.drawable.ic_close),
            enabled = enabled,
        )
    }
}

@MultiPreview
@Composable
private fun SwitchPreview() = PreviewBody {
    Row(
        modifier = Modifier.padding(12.dp)
    ) {
        SwitchPreviewColumn(
            enabled = true
        )
        Spacer(modifier = Modifier.width(16.dp))
        SwitchPreviewColumn(
            enabled = false
        )
    }
}
