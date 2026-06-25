package org.example.app.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle

object CreateIssueResultContract {
    private const val KEY: String = "create_issue"

    fun set(handle: SavedStateHandle) {
        handle[KEY] = true
    }

    fun observe(
        handle: SavedStateHandle,
        owner: LifecycleOwner,
        onResult: () -> Unit
    ) {
        handle.getLiveData<Boolean>(KEY).observe(owner) { value ->
            if (value == true) {
                onResult()
                handle.remove<Boolean>(KEY)
            }
        }
    }
}
