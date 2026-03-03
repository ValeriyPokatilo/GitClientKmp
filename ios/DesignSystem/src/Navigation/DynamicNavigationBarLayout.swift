//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

private extension CGFloat {
    static let titleHeight: CGFloat = 80.0
    static let titlePadding: CGFloat = 24.0
}

public enum NavigationBarItem {
    /// Кнопка назад
    case back(action: () -> Void)
    /// Кнопка закрытия
    case close(action: () -> Void)
    /// Кнопка в виде текста
    case plain(text: String, action: () -> Void)
    /// Кнопка в виде иконки
    case icon(icon: Image?, action: (() -> Void)?)
    /// Просто текст, не кнопка
    case text(text: String)
    /// Текст с иконкой
    case textWithIcon(text: String, icon: Image)
}

/**
 Лейаут с панелью навигации, заменяет полностью нативные navigationBar.
 Свой лейаут навигации сделан потому что нужно многострочные заголовки экранов и анимацию
 перехода названия из тела скролла в панель навигации.

 Поддерживает динамическую смену контента с скроллящейся, на статичную (например прогресс
 бар по центру, или еще что подобное).

 Для работы механики динамического перемещения заголовка из скролла в навбар недостаточно
 просто включить `isContentScrollable` (но это обязательное условие), в дополнение нужно
 проставить специальные модификаторы на сам `ScrollView` и на контент `ScrollView`.
 - `navigationScroll` - на `ScrollView`
 - `navigationScrollableContent` - на контент `ScrollView`

 Работает это следующим образом:
 Если `isContentScrollable` включен, то заголовок навигации будет добавлен внутрь
 `ScrollView`, над контентом. Это сделает модификатор `navigationScrollableContent`.
 Далее должна работать механика отслеживания положения скролла. Для этого нужно знать
 положение контента в скролле, а для этого надо для скролла задать координатное пространство,
 на которое мы и будем ориентироваться - это сделает модификатор `navigationScroll`.
 Далее отслеживание позиции будет сравнивать пора ли отображать заголовок в навбаре статичном,
 а в скролле его скрывать. Это отслеживание делает модификатор
 `navigationScrollableContent`.
 */
public struct DynamicNavigationBarLayout<Content: View>: View {
    @Environment(\.appTheme) var theme

    private let title: String?
    private let isContentScrollable: Bool
    @ViewBuilder
    private let content: () -> Content
    private let backgroundColor: Color
    private let leftBarItem: NavigationBarItem?
    private let rightBarItem: NavigationBarItem?

    @StateObject private var context = DynamicNavigationContext()

    public init(
        title: String?,
        isContentScrollable: Bool,
        backgroundColor: Color = Color.white,
        leftBarItem: NavigationBarItem? = nil,
        rightBarItem: NavigationBarItem? = nil,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.title = title
        self.isContentScrollable = isContentScrollable
        self.content = content
        self.backgroundColor = backgroundColor
        self.leftBarItem = leftBarItem
        self.rightBarItem = rightBarItem
    }

    public var body: some View {
        VStack(spacing: .zero) {
            DynamicNavigationBarView(
                backgroundColor: self.backgroundColor,
                leftBarItem: self.leftBarItem,
                rightBarItem: self.rightBarItem
            ).environmentObject(self.context)

            NavigationContentView(
                isContentScrollable: self.isContentScrollable
            ) {
                content()
                    .environmentObject(context)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .navigationBarHidden(true)
                    .navigationBarBackButtonHidden()
            }
            .environmentObject(self.context)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        // appear + change чтобы и начальное значение и новые получать
        // а сама логика эта нужна чтобы в внутренние view прокидывать
        // заголовок
        .onAppear {
            context.title = title
        }
        .onChange(of: title) {
            context.title = $0
        }
    }
}

/**
 Контент навигации, в нём заголовок навигации перед кастомным телом отображается, если у нас
 режим со скроллом сейчас.
 Важно чтобы контекст передавался сюда как EnvironmentObject - это позволяет View подписаться
 на изменения Published переменных контекста и пересоздаваться в нужный момент.
 */
private struct NavigationContentView<Content: View>: View {
    @EnvironmentObject private var context: DynamicNavigationContext

    let isContentScrollable: Bool
    @ViewBuilder
    let contentView: () -> Content

    var body: some View {
        VStack(spacing: .titlePadding) {
            if let title = context.title, !isContentScrollable {
                NavigationTitle(title: title, mode: .large)
                    .padding(.horizontal)
            }

            contentView()
        }
    }
}

/**
 Верхняя панель навигации с кнопками слева и справа.
 */
public struct NavigationBarView: View {
    @Environment(\.appTheme) private var theme
    let backgroundColor: Color
    let title: String?
    let titleMode: NavigationTitle.TitleMode
    let leftBarItem: NavigationBarItem?
    let rightBarItem: NavigationBarItem?

    public init(
        backgroundColor: Color = Color.white,
        title: String?,
        titleMode: NavigationTitle.TitleMode = .inline,
        leftBarItem: NavigationBarItem? = nil,
        rightBarItem: NavigationBarItem? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.title = title
        self.titleMode = titleMode
        self.leftBarItem = leftBarItem
        self.rightBarItem = rightBarItem
    }

    public var body: some View {
        HStack(alignment: .center, spacing: .zero) {
            if let leftBarItem = leftBarItem {
                NavigationBarItemView(item: leftBarItem)
                    .padding(.leading)
            }

            if let title = title {
                NavigationTitle(title: title, mode: .inline)
                    .opacity(titleMode == .large ? .zero : 1.0)
                    .frame(height: .titleHeight)
                    .padding(.horizontal)
            } else {
                Spacer()
            }

            if let rightBarItem = rightBarItem {
                NavigationBarItemView(item: rightBarItem)
                    .padding(.trailing)
            }
        }
        .frame(height: .titleHeight)
        .background { backgroundColor.shadow(color: titleMode == .inline
                ? theme.colors.shadowColor
                : .clear, radius: 4, x: 0, y: 4)
        }
    }
}

/**
 Верхняя панель навигации с автоматическим показом/скрытием заголовка, в случае когда он
 не в режиме .large.
 Важно чтобы контекст передавался сюда как EnvironmentObject - это позволяет View подписаться
 на изменения Published переменных контекста и пересоздаваться в нужный момент.
 */
private struct DynamicNavigationBarView: View {
    @EnvironmentObject private var context: DynamicNavigationContext

    let backgroundColor: Color
    let leftBarItem: NavigationBarItem?
    let rightBarItem: NavigationBarItem?

    var body: some View {
        NavigationBarView(
            backgroundColor: self.backgroundColor,
            title: context.title,
            titleMode: context.titleMode,
            leftBarItem: self.leftBarItem,
            rightBarItem: self.rightBarItem
        )
    }
}

private struct NavigationBarItemView: View {
    @Environment(\.appTheme) var theme
    let item: NavigationBarItem

    var body: some View {
        switch item {
        case let .back(action):
            CircularIconButton(image: .chevronBackward, action: action)
        case let .close(action):
            CircularIconButton(image: .cross, action: action)
        case let .plain(text, action):
            Button(
                action: action,
                label: {
                    Text(text)
                        .font(theme.typography.bold(size: 16))
                        .foregroundColor(theme.colors.primaryColor)
                }
            )
        case let .icon(icon, action):
            Button(
                action: action ?? {},
                label: {
                    icon
                }
            )
        case let .text(text):
            Text(text)
                .foregroundStyle(theme.colors.onSurfaceColor)
                .font(theme.typography.regular(size: 16))
        case let .textWithIcon(text, icon):
            HStack(spacing: 4) {
                icon

                Text(text)
                    .font(theme.typography.segmentButton)
                    .foregroundStyle(theme.colors.primaryColor)
            }
        }
    }
}

/**
 Контекст нам нужен для обмена информацией между разными View для обеспечения
 нужной нам логики. Он передается через environmentObject, поэтому разные view получают один
 и тот же инстанс и могут общаться друг с другом
 */
private class DynamicNavigationContext: ObservableObject {
    /// режим работы заголовка - если Large значит рисуется внутри скролла, если inline - сверху
    @Published var titleMode: NavigationTitle.TitleMode = .large
    /// текст заголовка. Фактически у нас есть несколько место где заголовок выводится
    /// (навбар и внутри скролла), поэтому нужно эту информацию проносить по иерархии view
    @Published var title: String? = nil
    /// название координатного пространства для работы отслеживания положения контента в
    /// скролле
    let coordinateSpaceName = UUID()
}

private struct ScrollableContentModifier: ViewModifier {
    @EnvironmentObject private var context: DynamicNavigationContext
    @State private var largeTitleHeight: CGFloat = 0

    func body(content: Content) -> some View {
        return VStack(spacing: .titlePadding) {
            if let title = context.title {
                NavigationTitle(title: title, mode: .large)
                    .padding(.horizontal)
                    // важно навешивать именно как overlay, чтобы измерения у
                    // NavigationTitle не ломались
                    .overlay {
                        GeometryReader { barProxy in
                            Color.clear
                                // комбинация Appear + Change дает нам
                                // отслеживание и первого значения и будущих
                                // изменений
                                .onAppear {
                                    largeTitleHeight = barProxy.size.height
                                }
                                .onChange(of: barProxy.size.height) { newHeight in
                                    largeTitleHeight = newHeight
                                }
                        }
                    }
            }

            content
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        // важно навешивать именно как overlay, чтобы измерения у
        // тела не ломались
        .overlay {
            GeometryReader { rootProxy in
                let rootFrame = rootProxy.frame(in: .named(context.coordinateSpaceName))
                let newOffset = rootFrame.minY

                // здесь как раз мы отслеживаем что положение контента внутри
                // скролла поменялось
                Color.clear
                    .onAppear {
                        updateTitleMode(newOffset)
                    }
                    .onChange(of: rootFrame.minY) { newOffset in
                        updateTitleMode(newOffset)
                    }
            }
        }
    }

    // чтобы не было ситуации когда уже не видно заголовок в
    // скролле и еще не видно заголовок в навбаре - пораньше
    // включаем заголовок в навбар, когда уже только 30% осталось
    private func updateTitleMode(_ newOffset: CGFloat) {
        if newOffset >= -largeTitleHeight * 0.7 {
            if context.titleMode != .large {
                context.titleMode = .large
            }
        } else if context.titleMode != .inline {
            context.titleMode = .inline
        }
    }
}

private struct NavigationScrollModifier: ViewModifier {
    @EnvironmentObject private var context: DynamicNavigationContext

    func body(content: Content) -> some View {
        return content
            // координатное пространство задаем чтобы по нему определять
            // положение контента в скролле
            .coordinateSpace(name: context.coordinateSpaceName)
    }
}

public extension View {
    func navigationScrollableContent() -> some View {
        modifier(ScrollableContentModifier())
    }
}

public extension ScrollView {
    func navigationScroll() -> some View {
        modifier(NavigationScrollModifier())
    }
}

// MARK: - Previews

#Preview("Simple") {
    DynamicNavigationBarLayout(
        title: "Simple with scroll",
        isContentScrollable: true,
        backgroundColor: Color.gray,
        leftBarItem: .back(action: {})
    ) {
        ScrollView {
            VStack {
                Text("Test\n\n\n\n\n")
                Text("Test 2")
            }.navigationScrollableContent()
        }.navigationScroll()
    }
}

#Preview("Center box") {
    DynamicNavigationBarLayout(
        title: "Test title",
        isContentScrollable: false,
        backgroundColor: .gray,
        leftBarItem: .back(action: {})
    ) {
        ZStack(alignment: .center) {
            Text("Center text")
        }
    }
}

// @Previewable доступно только с Xcode 16, поэтому проверяем что мы в Xcode 16 сейчас. Косвенно - по используемому компилятору
#if compiler(>=6)

    @available(iOS 17.0, *)
    #Preview("States") {
        @Previewable @State var state: Int = 0

        DynamicNavigationBarLayout(
            title: "Test title",
            isContentScrollable: state != 1,
            leftBarItem: .back(action: {})
        ) {
            switch state {
            case 0:
                ScrollView {
                    VStack {
                        Text("Test\n\n\n\n\n")
                        Text("Test 2")
                        Button(
                            action: { state = 1 }
                        ) {
                            Text("Test")
                        }
                    }.navigationScrollableContent()
                }.navigationScroll()
            case 1:
                ZStack(alignment: .center) {
                    VStack {
                        Text("Center text")
                        Button(action: { state = 2 }) {
                            Text("Change state")
                        }
                    }
                }
            default:
                VStack {
                    ScrollView {
                        VStack {
                            Text("Test\n\n\n\n\n")
                            Text("Test 2")
                            Button(
                                action: { state = 0 }
                            ) {
                                Text("Test")
                            }
                        }.navigationScrollableContent()
                    }.navigationScroll()

                    Text("some bottom text")
                }
            }
        }
    }

#endif
