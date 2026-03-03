//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

import MultiPlatformLibrary

struct PagingContentView<Item: Identifiable & AnyObject, Content: View>: View {
    private let state: PagingState<Item>
    private let onLoadNextRequested: () -> Void
    private let spacing: CGFloat?
    private let content: (Item) -> Content

    init(
        state: PagingState<Item>,
        onLoadNextRequested: @escaping () -> Void,
        spacing: CGFloat? = 16,
        @ViewBuilder content: @escaping (Item) -> Content
    ) {
        self.state = state
        self.onLoadNextRequested = onLoadNextRequested
        self.spacing = spacing
        self.content = content
    }

    var body: some View {
        LazyVStack(spacing: spacing) {
            ForEach(state.items.indices, id: \.self) { index in
                content(state.items[index] as! Item)
                    .onAppear {
                        if index == state.items.indices.last {
                            guard !state.isEndOfList, !state.isNextPageLoading else { return }

                            onLoadNextRequested()
                        }
                    }
            }

            ProgressView()
                .scaleEffect(1.5)
                .opacity(state.isNextPageLoading ? 1.0 : .zero)
        }
        .padding(.horizontal, 16)
    }
}

struct RemoteStateContentView<Data: AnyObject, Content: View>: View {
    let state: RemoteState<Data, ErrorBundle>
    @ViewBuilder let content: (Data) -> Content
    let onRetry: (() -> Void)?

    var body: some View {
        switch onEnum(of: state) {
        case .loading:
            CenteredProgressBar()
        case let .success(data):
            content(data.data)
        case let .error(obj):
            ErrorStateView(
                title: obj.error.title.localized(),
                message: obj.error.message.localized(),
                onRetryPressed: onRetry
            )
        }
    }
}

/// Вью RemoteState с поддержкой состояния пустоты
struct RemoteStateContentViewExt<Data: AnyObject, Content: View, Empty: View>: View {
    let state: RemoteState<Data, ErrorBundle>
    let emptyStateCondition: (Data) -> Bool
    let emptyStateView: Empty
    @ViewBuilder let content: (Data) -> Content
    let onRetry: (() -> Void)?

    init(
        state: RemoteState<Data, ErrorBundle>,
        emptyStateCondition: @escaping (Data) -> Bool,
        emptyStateView: Empty = EmptyStateView(),
        content: @escaping (Data) -> Content,
        onRetry: (() -> Void)?
    ) {
        self.state = state
        self.emptyStateCondition = emptyStateCondition
        self.content = content
        self.onRetry = onRetry
        self.emptyStateView = emptyStateView
    }

    var body: some View {
        RemoteStateContentView(
            state: state,
            content: { data in
                if emptyStateCondition(data) {
                    emptyStateView
                } else {
                    content(data)
                }
            },
            onRetry: onRetry
        )
    }
}

struct EmptyStateView: View {
    let action: (() -> Void)? = nil

    var body: some View {
        VStack(spacing: 16) {
            if let action = action {
                Button("Retry") {
                    action()
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
        .padding()
    }
}

struct ErrorStateView: View {
    let title: String
    let message: String
    let onRetryPressed: (() -> Void)?

    var body: some View {
        ZStack {
            Color.white.ignoresSafeArea()
            Text("Error")
                .font(.largeTitle)
                .foregroundColor(.red)
        }
        .navigationBarHidden(true)
    }
}

struct CenteredProgressBar: View {
    var body: some View {
        ZStack {
            Color.clear
            ProgressView()
                .controlSize(.large)
        }
    }
}
