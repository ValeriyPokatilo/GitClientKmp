//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI
import UIKit
import WebKit

// не поддерживается:
// выравнивание - центр, право
// sup (в метрах квадратных например)
// списки
// не растягивается на всю ширину родителя
public struct SUIHtmlText: View {
    let html: AttributedString

    public init(
        html: String,
        convertError: @escaping (Error) -> AttributedString = { _ in .init("") }
    ) {
        do {
            let styledHtml = applyThemeStylesToHtml(html)
            let styledString = try convertHTMLToAttributedString(styledHtml)
            self.html = AttributedString(styledString)
        } catch {
            self.html = convertError(error)
        }
    }

    public var body: some View {
        Text(html)
    }
}

// рекомендованный путь отрисовки форматирования текста,
// если надо встроить в экран текст html без картинок
public struct UIKHtmlText: UIViewRepresentable {
    public typealias UIViewType = UILabel

    let html: NSAttributedString

    public init(
        html: String,
        convertError: @escaping (Error) -> NSAttributedString = { _ in .init("") }
    ) {
        do {
            let styledHtml = applyThemeStylesToHtml(html)
            self.html = try convertHTMLToAttributedString(styledHtml)
        } catch {
            self.html = convertError(error)
        }
    }

    public func makeUIView(context _: Context) -> UILabel {
        let uiView = UILabel(frame: .zero)
        uiView.translatesAutoresizingMaskIntoConstraints = false
        uiView.numberOfLines = 0
        uiView.lineBreakMode = .byWordWrapping
        uiView.setContentHuggingPriority(.defaultHigh, for: .vertical)
        uiView.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        return uiView
    }

    public func updateUIView(_ uiView: UILabel, context _: Context) {
        uiView.attributedText = html
    }

    public func sizeThatFits(
        _ proposal: ProposedViewSize,
        uiView: UILabel,
        context _: Context
    ) -> CGSize? {
        guard let width = proposal.width else { return nil }

        return uiView.sizeThatFits(
            CGSize(
                width: width,
                height: .greatestFiniteMagnitude
            )
        )
    }
}

public struct WKHtmlText: UIViewRepresentable {
    public typealias UIViewType = WKWebView

    private let html: String

    public init(
        html: String
    ) {
        self.html = applyThemeStylesToHtml(html)
    }

    public func makeUIView(context: Context) -> WKWebView {
        let uiView = WKWebView(frame: .zero)
        uiView.navigationDelegate = context.coordinator
        uiView.backgroundColor = .red
        return uiView
    }

    public func updateUIView(_ uiView: WKWebView, context _: Context) {
        uiView.loadHTMLString(html, baseURL: nil)
    }

    public func makeCoordinator() -> Coordinator {
        return Coordinator()
    }

    public class Coordinator: NSObject, WKNavigationDelegate {
        var height: CGFloat?

        public func webView(
            _ webView: WKWebView,
            didFinish _: WKNavigation!
        ) {
            webView.evaluateJavaScript("document.body.scrollHeight") { result, _ in
                if let height = result as? CGFloat {
                    self.height = height
                    webView.invalidateIntrinsicContentSize()
                }
            }
        }
    }

    public func sizeThatFits(_ proposal: ProposedViewSize, uiView _: WKWebView, context: Context) -> CGSize? {
        guard let width = proposal.width else { return nil }

        return CGSize(
            width: width,
            height: context.coordinator.height ?? 0.0
        )
    }
}

internal func applyThemeStylesToHtml(_ html: String) -> String {
    return """
    <html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=0, maximum-scale=0">
    <style>
        @font-face {
            font-family: 'Manrope';
            src: url('Manrope-Regular.ttf') format('truetype');
        }
        body {
            font-family: 'Manrope', -apple-system, sans-serif;
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
    \(html)
    </body>
    </html>
    """
}

internal func convertHTMLToAttributedString(
    _ html: String
) throws -> NSAttributedString {
    guard let data = html.data(using: .utf8) else {
        throw HtmlTextError.htmlTextInvalid
    }

    let nsAttributedString = try NSAttributedString(
        data: data,
        options: [
            .documentType: NSAttributedString.DocumentType.html,
            .characterEncoding: String.Encoding.utf8.rawValue,
        ],
        documentAttributes: nil
    )
    return nsAttributedString
}

public enum HtmlTextError: Error {
    case htmlTextInvalid
}

private let previewHtml = """
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

#Preview("SwiftUI") {
    AppThemeProvider {
        ScrollView {
            VStack {
                SUIHtmlText(html: previewHtml)
                    .padding()
            }
        }
    }
}

// в превью списки не отрисовывают буллеты и числа,
// но в приложении успешно рисуют почему-то
#Preview("UIKit") {
    AppThemeProvider {
        ScrollView {
            VStack {
                UIKHtmlText(html: previewHtml)
                    .padding()
            }
        }
    }
}

#Preview("WebKit") {
    AppThemeProvider {
        ScrollView {
            VStack {
                WKHtmlText(html: previewHtml)
                    .padding()
            }
        }
    }
}
