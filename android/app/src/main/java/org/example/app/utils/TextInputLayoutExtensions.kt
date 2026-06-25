package org.example.app.utils

import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.textfield.TextInputLayout
import dev.icerock.moko.fields.livedata.FormField
import dev.icerock.moko.mvvm.livedata.Closeable
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.bindTextTwoWay
import dev.icerock.moko.mvvm.utils.bind
import dev.icerock.moko.resources.desc.StringDesc

fun TextInputLayout.bindField(
    lifecycleOwner: LifecycleOwner,
    formField: FormField<String, StringDesc>
) {
    val editText: EditText = editText ?: return

    editText.bindTextTwoWay(lifecycleOwner, formField.data)
    editText.setOnFocusChangeListener { _, focused ->
        if (focused) return@setOnFocusChangeListener
        formField.validate()
    }
    this.bindError(lifecycleOwner, formField.error)
}

fun <T : StringDesc?> TextInputLayout.bindError(
    lifecycleOwner: LifecycleOwner,
    liveData: LiveData<T>
): Closeable {
    return liveData.bind(lifecycleOwner) {
        this.error = it?.toString(this.context)
        this.isErrorEnabled = it != null
    }
}
