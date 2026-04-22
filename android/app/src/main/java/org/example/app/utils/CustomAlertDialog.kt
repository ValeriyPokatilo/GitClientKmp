package org.example.app.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import org.example.app.databinding.AlertFragmentBinding

object CustomAlertDialog {
    fun create(
        context: Context,
        title: String,
        message: String,
        buttonTitle: String
    ): AlertDialog {
        val binding = AlertFragmentBinding.inflate(LayoutInflater.from(context))

        with(binding) {
            dialogTitle.text = title
            dialogMessage.text = message
            dialogButton.text = buttonTitle
        }

        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(true)
            .create().apply {
                binding.dialogButton.setOnClickListener {
                    dismiss()
                }
            }
    }
}


