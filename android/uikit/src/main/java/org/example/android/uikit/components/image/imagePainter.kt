package org.example.android.uikit.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.ImageDescResource
import dev.icerock.moko.resources.desc.image.ImageDescUrl

@Composable
fun imagePainter(
    imageDesc: ImageDesc,
    placeholder: Painter? = null,
    fallback: Painter? = null,
    error: Painter? = null,
): Painter {
    return when (imageDesc) {
        is ImageDescUrl -> rememberAsyncImagePainter(
            model = imageDesc.url,
            placeholder = placeholder,
            fallback = fallback,
            error = error,
        )

        is ImageDescResource -> painterResource(imageDesc.resource)
        else -> error("unknown image desc $imageDesc")
    }
}
