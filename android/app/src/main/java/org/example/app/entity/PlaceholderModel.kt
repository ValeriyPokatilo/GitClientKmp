package org.example.app.entity

import android.R
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class PlaceholderModel(
    @DrawableRes val iconRes: Int,
    val title: String,
    val message: String,
    @ColorRes val titleColorRes: Int = R.color.white,
    val buttonTitle: String,
    val buttonAction: (() -> Unit)
)