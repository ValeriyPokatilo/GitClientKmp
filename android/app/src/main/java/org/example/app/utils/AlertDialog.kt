package org.example.app.utils

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import org.example.app.databinding.AlertFragmentBinding

fun Fragment.showErrorAlertDialog(
    title: String,
    message: String,
    buttonTitle: String
) {
    val context = context ?: return

    val binding = AlertFragmentBinding.inflate(
        LayoutInflater.from(context)
    )

    with(binding) {
        dialogTitle.text = title
        dialogMessage.text = message
        dialogButton.text = buttonTitle
    }

    val dialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .setCancelable(true)
        .create()

    binding.dialogButton.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}
