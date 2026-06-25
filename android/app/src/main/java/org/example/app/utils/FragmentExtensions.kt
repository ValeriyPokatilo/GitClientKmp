package org.example.app.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import app.xl.gitclientkmp.domain.error.ErrorModel
import org.example.app.R
import org.example.app.databinding.AlertFragmentBinding

fun Fragment.openUrl(url: String) {
    if (url.isBlank()) return

    val uri = runCatching { url.toUri() }.getOrNull() ?: return
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val context = requireContext()

    if (intent.resolveActivity(context.packageManager) != null) {
        startActivity(intent)
    }
}

fun Fragment.showErrorAlertDialog(errorModel: ErrorModel) {
    val context: Context = context ?: return

    val binding: AlertFragmentBinding = AlertFragmentBinding.inflate(
        LayoutInflater.from(context)
    )

    val dialog: AlertDialog = CustomAlertDialog
        .create(
            context = context,
            title = getString(R.string.error),
            message = errorModel.alertMessage?.toString(requireContext()).orEmpty(),
            buttonTitle = getString(R.string.ok)
        )

    binding.dialogButton.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}
