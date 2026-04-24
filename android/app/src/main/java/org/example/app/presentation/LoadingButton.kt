package org.example.app.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.example.app.R

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val button: MaterialButton
    private val progress: CircularProgressIndicator

    init {
        LayoutInflater.from(context).inflate(
            R.layout.loading_button_fragment,
            this,
            true
        )
        button = findViewById(R.id.button)
        progress = findViewById(R.id.progressIndicator)
    }

    fun setTitle(title: String) {
        button.text = title
    }

    fun setButtonClickListener(listener: OnClickListener) {
        button.setOnClickListener(listener)
    }

    fun startLoading() {
        button.isEnabled = false
        progress.visibility = VISIBLE
        button.setTextColor(Color.TRANSPARENT)
    }

    fun stopLoading() {
        button.isEnabled = true
        progress.visibility = GONE
        button.setTextColor(Color.WHITE)
    }

    fun setPrimaryStyle() {
        button.setTextColor(ContextCompat.getColor(context, R.color.white))
        button.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
    }

    fun setSecondaryStyle() {
        button.setTextColor(ContextCompat.getColor(context, R.color.light_green))
        button.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        button.strokeColor = ColorStateList.valueOf(Color.WHITE)
        button.strokeWidth = 1
    }
}
