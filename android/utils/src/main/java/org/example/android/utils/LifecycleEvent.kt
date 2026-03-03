package org.example.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OnLifecycleCreateEvent(onEvent: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == ON_CREATE) {
            onEvent()
        }
    }
}

@Composable
fun OnLifecycleStartEvent(onEvent: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == ON_START) {
            onEvent()
        }
    }
}

@Composable
fun OnLifecycleResumeEvent(onEvent: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == ON_RESUME) {
            onEvent()
        }
    }
}

@Composable
fun OnLifecyclePauseEvent(onEvent: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == ON_PAUSE) {
            onEvent()
        }
    }
}

@Composable
fun OnLifecycleStopEvent(onEvent: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == ON_STOP) {
            onEvent()
        }
    }
}

@Composable
fun OnLifecycleDestroyEvent(onEvent: () -> Unit) {
    OnLifecycleEvent { _, event ->
        if (event == ON_DESTROY) {
            onEvent()
        }
    }
}
