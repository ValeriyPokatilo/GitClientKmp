package org.example.app.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import app.xl.gitclientkmp.domain.error.ErrorModel
import org.example.app.R
import org.example.app.databinding.PlaceholderViewBinding
import org.example.app.model.PlaceholderState

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: PlaceholderViewBinding = PlaceholderViewBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        visibility = GONE
    }

    fun render(state: PlaceholderState) {
        when (state) {
            PlaceholderState.Hidden -> hide()

            is PlaceholderState.Empty -> showEmpty(
                title = state.title,
                message = state.message
            )

            is PlaceholderState.Error -> showError(state.error)
        }
    }

    private fun showEmpty(title: String, message: String) {
        visibility = VISIBLE

        binding.apply {
            placeholderIcon.setImageResource(R.drawable.ic_empty)
            placeholderTitle.text = title
            placeholderTitle.setTextColor(ContextCompat.getColor(context, R.color.blue))
            placeholderMessage.text = message
        }
    }

    private fun showError(error: ErrorModel) {
        visibility = VISIBLE

        val iconRes: Int = if (error.isNetworkError) {
            R.drawable.ic_not_connected
        } else {
            R.drawable.ic_error
        }

        val titleText: String = error.title.toString(context = context)
        val messageText: String = error.message.toString(context = context)
        val titleColorRes: Int = R.color.error

        binding.apply {
            placeholderIcon.setImageResource(iconRes)
            placeholderTitle.text = titleText
            placeholderTitle.setTextColor(ContextCompat.getColor(context, titleColorRes))
            placeholderMessage.text = messageText
        }
    }

    private fun hide() {
        visibility = GONE
        binding.apply {
            placeholderIcon.setImageDrawable(null)
            placeholderTitle.text = null
            placeholderTitle.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            placeholderMessage.text = null
        }
    }
}
