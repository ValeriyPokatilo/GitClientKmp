package org.example.uisamples.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class LongAndShortTitlesProvider : PreviewParameterProvider<String> {

    override val values: Sequence<String>
        get() = sequenceOf(
            "Заголовок",
            "Заголовок с длинным текстом для тестирования реакции компонентов на отображение длянных текстов"
        )
}
