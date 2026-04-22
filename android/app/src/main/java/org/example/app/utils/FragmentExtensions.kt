package org.example.app.utils

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

fun Fragment.showErrorAlertDialog(
    title: String,
    message: String,
    buttonTitle: String
) {
    val context = context ?: return

    CustomAlertDialog
        .create(
            context = context,
            title = title,
            message = message,
            buttonTitle = buttonTitle
        )
        .show()
}