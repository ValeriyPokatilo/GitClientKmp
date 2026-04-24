package org.example.app.model

import app.xl.gitclientkmp.domain.error.ErrorModel

sealed interface PlaceholderState {
    data object Hidden : PlaceholderState

    data class Empty(
        val title: String,
        val message: String
    ) : PlaceholderState

    data class Error(
        val error: ErrorModel
    ) : PlaceholderState
}