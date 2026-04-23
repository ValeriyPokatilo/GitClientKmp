package org.example.app.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import app.xl.gitclientkmp.domain.error.ErrorModel
import org.example.app.R
import org.example.app.databinding.PlaceholderViewBinding

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: PlaceholderViewBinding = PlaceholderViewBinding.inflate(
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
            placeholderTitle.text = getString(context, R.string.repositories_empty_title)
            placeholderTitle.setTextColor(ContextCompat.getColor(context, R.color.blue))
            placeholderMessage.text = getString(context, R.string.repositories_empty_message)
            button.text = getString(context, R.string.refresh)
            button.visibility = VISIBLE
        }

        this.action = action
        binding.button.setOnClickListener { this.action?.invoke() }
    }

    fun showError(error: ErrorModel, action: () -> Unit) {
        reset()
        visibility = VISIBLE

        val iconRes: Int = if (error.isNetworkError) {
            R.drawable.ic_not_connected
        } else {
            R.drawable.ic_error
        }
        val titleText: String = error.title.toString(context)
        val messageText: String = error.message.toString(context)
        val titleColorRes: Int = R.color.error

        binding.apply {
            placeholderIcon.setImageResource(iconRes)
            placeholderTitle.text = titleText
            placeholderTitle.setTextColor(ContextCompat.getColor(context, titleColorRes))
            placeholderMessage.text = messageText
            button.text = R.string.retry.toString()
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
