package org.example.app.utils

import android.app.AlertDialog
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.example.app.R

fun Fragment.showErrorAlertDialog(
    title: String,
    message: String,
    buttonTitle: String
) {
    val dialogView = layoutInflater.inflate(R.layout.fragment_alert, null)

    val titleTextView = dialogView.findViewById<TextView>(R.id.dialogTitle)
    val messageTextView = dialogView.findViewById<TextView>(R.id.dialogMessage)
    val okButton = dialogView.findViewById<Button>(R.id.dialogButton)

    titleTextView.text = title
    messageTextView.text = message
    okButton.text = buttonTitle

    val dialog = AlertDialog.Builder(requireContext())
        .setView(dialogView)
        .create()

    okButton.setOnClickListener { dialog.dismiss() }

    dialog.show()
}
