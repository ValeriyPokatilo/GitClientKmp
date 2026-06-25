package org.example.app.utils

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.SpanFactory
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.image.coil.CoilImagesPlugin
import org.commonmark.node.Heading
import org.example.app.R

object MarkwonFactory {
    fun createMarkwon(context: Context): Markwon {
        return Markwon.builder(context)
            .usePlugin(CoilImagesPlugin.create(context))
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    val linkColor: Int = ContextCompat.getColor(context, R.color.blue)
                    builder
                        .headingBreakHeight(0)
                        .linkColor(linkColor)
                }

                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    val origin: SpanFactory? = builder.getFactory(Heading::class.java)
                    builder.setFactory(Heading::class.java) { configuration, props ->
                        arrayOf(
                            origin?.getSpans(configuration, props),
                            ForegroundColorSpan(Color.WHITE)
                        )
                    }
                }
            })
            .build()
    }
}
