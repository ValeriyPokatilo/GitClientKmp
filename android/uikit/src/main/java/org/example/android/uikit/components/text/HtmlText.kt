@file:Suppress("CommentWrapping")

package org.example.android.uikit.components.text

import android.annotation.SuppressLint
import android.text.Html
import android.text.Spanned
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody

/**
 * Отрисовка Spanned текста (от Android View), работает через рисование
 * TextView (старого Android View).
 *
 * Нужен потому что Spanned поддерживает больше элементов форматирования
 * чем AnnotatedString.
 */
@Composable
fun SpannedText(
    spanned: Spanned,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                @Suppress("MagicNumber")
                textSize = 16f
                setTextColor(android.graphics.Color.BLACK)
            }
        },
        update = { textView ->
            textView.text = spanned
        }
    )
}

/**
 * Отрисовка html через AnnotatedString. Чистый Compose Text, без Android View.
 *
 * Проблема этого подхода в том что не поддерживается:
 * 1. списки (bullet / numbered)
 * 2. параграфы не имеют отступов
 * 3. выравнивание
 */
@Composable
fun AnnotatedHtmlText(
    html: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = remember(html) {
            AnnotatedString.fromHtml(html)
        },
        modifier = modifier
    )
}

/**
 * Отрисовка html через Spanned. Работает через Android View.
 *
 * Проблема этого подхода в том что не поддерживается:
 * 1. список numbered
 * 2. выравнивание
 */
@Composable
fun SpannedHtmlText(
    html: String,
    modifier: Modifier = Modifier
) {
    SpannedText(
        spanned = remember(html) {
            // https://www.ericthecoder.com/2020/06/22/android-textview-spannable-cheat-sheet/
            // https://developer.android.com/develop/ui/views/text-and-emoji/spans
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        },
        modifier = modifier
    )
}

/**
 * Отрисовка html через WebView, с автоматическим подгоном высоты view под
 * всё содержимое WebView.
 *
 * Проблема этого подхода в том, что у нас тяжелое WebView - в списки пихать не надо такое.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewHtmlText(
    html: String,
    modifier: Modifier = Modifier
) {
    var webViewHeight: Int by remember { mutableIntStateOf(0) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // нам нужен js для получения размера
                settings.javaScriptEnabled = true
                // Заставляет WebView подстраивать масштаб страницы, чтобы она целиком помещалась
                // в доступное пространство (ширину устройства).
                settings.loadWithOverviewMode = true
                // Позволяет странице задавать размеры и масштабирование через мета-тег
                // <meta name="viewport" ...>, делая контент более адаптивным.
                settings.useWideViewPort = true

                // иначе при загрузке будет мелькать индикатор
                isHorizontalScrollBarEnabled = false
                isVerticalScrollBarEnabled = false

                // Интерфейс для получения высоты контента
                addJavascriptInterface(
                    /* object = */ object {
                        @JavascriptInterface
                        fun setHeight(height: Int) {
                            webViewHeight = height
                        }
                    },
                    /* name = */ "AndroidInterface"
                )

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)

                        view.loadUrl("javascript:AndroidInterface.setHeight(document.body.scrollHeight)")
                    }
                }
            }
        },
        modifier = modifier.height(webViewHeight.dp),
        update = { webView ->
            webView.loadDataWithBaseURL(
                /* baseUrl = */ null,
                /* data = */ prepareHtmlBody(html),
                /* mimeType = */ "text/html",
                /* encoding = */ "utf-8",
                /* historyUrl = */ null
            )
        }
    )
}

private fun prepareHtmlBody(html: String) = """<html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <style>
        @font-face {
            font-family: 'Manrope';
            src: url('Manrope-Regular.ttf') format('truetype');
        }
        body {
            font-family: 'Manrope', sans-serif;
            font-size: 16px;
            color: #2F204B;
            margin: 0;
            padding: 0;
        }
        li p {
            margin: 0;
            padding: 0;
        }
        p {
            margin: 0 0 8px 0;
        }
    </style>
    </head>
    <body>
        $html
    </body>
</html>"""

private const val PREVIEW_HTML = """
<p>
<strong>Жирный</strong><br>
<em>Курсив</em><br>
<u>Подчеркнутый</u><br>
<s>Зачеркнутый</s>
</p>
<p></p>
<ul>
<li><p>Список 1</p></li>
<li><p>Список 2</p></li>
<li><p>Список 3</p><p></p></li>
</ul>
<ol>
<li><p>Список 1</p></li>
<li><p>Список 2</p></li>
<li><p>Список 3</p></li>
</ol>
<p>Площадь м<sup>2</sup></p>
<p>Лево</p>
<p align="center">Центр</p>
<p align="right">Право</p>
<p align="justify">По ширине текста</p>
"""

@MultiPreview
@Composable
private fun ComposeHtmlTextPreview() = PreviewBody {
    AnnotatedHtmlText(
        html = PREVIEW_HTML,
        modifier = Modifier.width(320.dp)
    )
}

@MultiPreview
@Composable
private fun SpannableHtmlTextPreview() = PreviewBody {
    SpannedHtmlText(
        html = PREVIEW_HTML,
        modifier = Modifier.width(320.dp)
    )
}

@MultiPreview
@Composable
private fun WebViewHtmlTextPreview() = PreviewBody {
    if (LocalInspectionMode.current) {
        Text("нажмите Run Preview для проверки")
    } else {
        WebViewHtmlText(
            html = PREVIEW_HTML,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
