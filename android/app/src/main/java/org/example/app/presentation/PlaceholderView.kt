package org.example.app.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.error.ErrorModel
import org.example.app.R
import org.example.app.databinding.ViewPlaceholderBinding
import org.example.app.extensions.toDrawableRes

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
            button.text = MR.strings.refresh.getString(context).uppercase()
            button.visibility = VISIBLE
        }

        this.action = action
        binding.button.setOnClickListener { this.action?.invoke() }
    }

    fun showError(error: ErrorModel, action: () -> Unit) {
        reset()
        visibility = VISIBLE

        val iconRes = error.icon.toDrawableRes()
        val titleText = error.title.toString(context)
        val messageText = error.message.toString(context)
        val titleColorRes = R.color.error

        binding.apply {
            placeholderIcon.setImageResource(iconRes)
            placeholderTitle.text = titleText
            placeholderTitle.setTextColor(ContextCompat.getColor(context, titleColorRes))
            placeholderMessage.text = messageText
            button.text = MR.strings.retry.getString(context).uppercase()
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
