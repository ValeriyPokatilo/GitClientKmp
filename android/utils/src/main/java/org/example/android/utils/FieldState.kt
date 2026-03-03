package org.example.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import dev.icerock.moko.fields.flow.FormField
import dev.icerock.moko.mvvm.flow.compose.collectAsMutableState
import dev.icerock.moko.resources.desc.StringDesc

@Immutable
data class FieldState<T>(
    val text: MutableState<T>,
    val error: State<StringDesc?>
)

@Composable
fun <D> FormField<D, StringDesc>.asFieldState(): FieldState<D> {
    return FieldState(
        text = this.data.collectAsMutableState(),
        error = this.error.collectAsState()
    )
}
