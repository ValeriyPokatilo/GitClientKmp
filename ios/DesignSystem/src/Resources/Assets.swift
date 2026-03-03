//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import UIKit

/// Swift Assets Extensions имеют проблемы:
/// - SwiftUI Preview почему-то обращатеся к Bundle некорректному, из-за чего ресурс не читается
/// - сгенерированные константы все приватные для фреймворка, а значит в приложениях не видны
///
/// R.Swift тоже нельзя использовать внутри фреймворка потому что:
/// - SwiftUI Preview не может найти RswiftResources фреймворк, потому что не умеет с
/// фреймворками у которых есть зависимости работать
///
/// Поэтому ведется рукописный файл такой, в котором добавляем свойства с картинками из
/// ресурсов дизайн системы.
///
/// тут ресурсы ТОЛЬКО ДИЗАЙН СИСТЕМЫ
public extension UIImage {
    /// только через ObjC мы надежно можем получить Bundle от framework. Судя по всему
    /// Swift по своей логике может удалить почему-то класс, по которому мы ищем framework
    /// и в результате мы получим через Bundle.forClass бандл основного приложения
    private static var resourceBundle: Bundle {
        return DSResourcesBundle.getBundle()!
    }

    static var exampleUserIcon: UIImage {
        UIImage(named: "user_icon", in: resourceBundle, with: nil)!
    }
}
