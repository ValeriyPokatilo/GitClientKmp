package org.example.app.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.entity.AppError
import org.example.app.R
import org.example.app.databinding.ViewPlaceholderBinding

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPlaceholderBinding = ViewPlaceholderBinding.inflate(
        LayoutInflater.from(context), this
    )

    private var action: (() -> Unit)? = null

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        visibility = GONE
    }

    fun showEmpty(action: () -> Unit) {
        reset()
        visibility = VISIBLE

        binding.apply {
            placeholderIcon.setImageResource(R.drawable.ic_empty)
            placeholderTitle.text = MR.strings.repositories_empty_title.getString(context)
            placeholderTitle.setTextColor(ContextCompat.getColor(context, R.color.blue))
            placeholderMessage.text = MR.strings.repositories_empty_message.getString(context)
            button.text = MR.strings.refresh.getString(context)
            button.visibility = VISIBLE
        }

        this.action = action
        binding.button.setOnClickListener { this.action?.invoke() }
    }

    fun showError(error: AppError, action: () -> Unit) {
        reset()
        visibility = VISIBLE

        val iconRes: Int
        val titleText: String
        val messageText: String
        val titleColorRes: Int

        when (error) {
            is AppError.Http -> {
                iconRes = R.drawable.ic_error
                titleText = error.code.toString()
                messageText = error.message ?: ""
                titleColorRes = R.color.error
            }

            is AppError.Network -> {
                iconRes = R.drawable.ic_not_connected
                titleText = MR.strings.repositories_connection_error_title.getString(context)
                messageText = MR.strings.repositories_connection_error_message.getString(context)
                titleColorRes = R.color.error
            }

            else -> {
                iconRes = R.drawable.ic_error
                titleText = ""
                messageText = error.message ?: ""
                titleColorRes = R.color.error
            }
        }

        binding.apply {
            placeholderIcon.setImageResource(iconRes)
            placeholderTitle.text = titleText
            placeholderTitle.setTextColor(ContextCompat.getColor(context, titleColorRes))
            placeholderMessage.text = messageText
            button.text = MR.strings.retry.getString(context)
            button.visibility = VISIBLE
        }

        this.action = action
        binding.button.setOnClickListener { this.action?.invoke() }
    }

    fun hide() {
        visibility = GONE
        reset()
    }

    private fun reset() {
        binding.apply {
            placeholderIcon.setImageDrawable(null)
            placeholderTitle.text = null
            placeholderTitle.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            placeholderMessage.text = null
            button.visibility = GONE
        }
        action = null
    }
}
