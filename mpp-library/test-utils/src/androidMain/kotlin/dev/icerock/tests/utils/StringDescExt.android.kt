package dev.icerock.tests.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dev.icerock.moko.resources.desc.StringDesc

actual fun StringDesc.toLocalizedString(): String {
    val context: Context = ApplicationProvider.getApplicationContext()
    return toString(context)
}
