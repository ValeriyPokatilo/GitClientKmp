# Предварительные требования

- MacOS для разработки под iOS
- JDK 17+
- Android Studio 2024.2.0+ - https://developer.android.com/studio
- Android SDK (устанавливается через Android Studio)
- Xcode 15+ (на macOS)
- Cocoapods 1.15+ (на macOS)
- Инструменты командной строки Xcode (на macOS - `xcode-select --install`)

# Сборка

## Сборка Android

Просто откройте проект в Android Studio и нажмите "Run". Или можно собрать проект в командной
строке - `./gradlew assembleDebug`.

## Сборка iOS

1. Установите зависимости Cocoapods `(cd ios && pod install)`;
2. Откройте рабочее пространство Xcode - `open ios/ios.xcworkspace` и запустите приложение (какое -
   выбирайте в scheme).
