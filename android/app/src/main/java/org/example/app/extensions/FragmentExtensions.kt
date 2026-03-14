package org.example.app.extensions

import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

fun Fragment.openUrl(url: String) {
    if (url.isBlank()) return

    val uri = runCatching { url.toUri() }.getOrNull() ?: return
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val context = requireContext()

    if (intent.resolveActivity(context.packageManager) != null) {
        startActivity(intent)
    }
}
