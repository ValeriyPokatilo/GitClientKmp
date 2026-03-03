//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import MultiPlatformLibrary
import SwiftUI

extension Image {
    static func appImage<LoadingView: View>(
        from imageDesc: ImageDesc,
        @ViewBuilder loadingView: @escaping () -> LoadingView = { ProgressView() },
        defaultImage: Image? = nil
    ) -> some View {
        Group {
            if let urlDesc = imageDesc as? ImageDescUrl, let url = URL(string: urlDesc.url) {
                AsyncImage(url: url) { phase in
                    switch phase {
                    case .empty:
                        loadingView()
                    case let .success(image):
                        image
                            .resizable()
                    case .failure:
                        if let defaultImage = defaultImage {
                            defaultImage
                                .resizable()
                        } else {
                            EmptyView()
                        }
                    @unknown default:
                        if let defaultImage = defaultImage {
                            defaultImage
                                .resizable()
                        } else {
                            EmptyView()
                        }
                    }
                }
            } else if let resourceDesc = imageDesc as? ImageDescResource {
                Image(resourceDesc.resource.assetImageName, bundle: resourceDesc.resource.bundle)
                    .resizable()
            } else {
                fatalError("Unsupported ImageDesc type: \(type(of: imageDesc))")
            }
        }
    }
}
