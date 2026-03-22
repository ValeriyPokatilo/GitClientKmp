package org.example.app.extensions

import app.xl.gitclientkmp.domain.error.ErrorIcon
import org.example.app.R

fun ErrorIcon.toDrawableRes(): Int {
    return when (this) {
        ErrorIcon.Network -> R.drawable.ic_not_connected
        ErrorIcon.Http -> R.drawable.ic_error
    }
}
