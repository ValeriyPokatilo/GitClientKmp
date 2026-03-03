# Настройка

## Настройка ApplicationId

- `android/app/build.gradle.kts:17`
- `ios-app/BuildConfigurations/ios-app.dev.xcconfig` - с постфиксом `.dev`
  `PRODUCT_BUNDLE_IDENTIFIER`, `PROVISIONING_PROFILE_SPECIFIER`
- `ios-app/BuildConfigurations/ios-app.stage.xcconfig` - с постфиксом `.stage`
  `PRODUCT_BUNDLE_IDENTIFIER`, `PROVISIONING_PROFILE_SPECIFIER`
- `ios-app/BuildConfigurations/ios-app.prod.xcconfig`
  `PRODUCT_BUNDLE_IDENTIFIER`, `PROVISIONING_PROFILE_SPECIFIER`

## Настройка приложений в Firebase

- Установите [Firebase CLI](https://firebase.google.com/docs/cli)
- Запустите скрипт настройки: `./setup-firebase-apps.sh <firebase_project_id> <bundle_id> <bundle_id_prod>`

Скрипт создаст приложения с `<bundle_id>.dev`, `<bundle_id>.stage` и `<bundle_id_prod>` для iOS и Android (для Android также создаются версии с *.debug для всех).
Отдельный параметр для production-бандла необходим в случаях, когда вы используете полностью другой бандл, не совпадающий с development. Если вам не нужен другой бандл, передайте одинаковые значения для `<bundle_id>` и `<bundle_id_prod>`.

- Скачайте `google-services.json` в `android/app/google-services.json`
- Скачайте все `GoogleService-Info.plist` в `ios-app/src/Firebase/GoogleService-Info-<bundle_id>.plist`

## Настройка подписи Android

- Удалите `android/app/signing/release.jks`
- Вызовите `./setup-android-signing.sh <key_name> <gitlab_token> <gitlab_project_id>`
- Закоммитьте и отправьте созданный файл

## Настройка деплоя Firebase

- Выполните команду `firebase login:ci`
- Скопируйте токен из результата
- Выполните `./setup-firebase-deploy.sh <gitlab_token> <gitlab_project_id> <firebase_token>`
- В разделе App Distribution консоли Firebase выберите приложение, которое хотите распространять, и нажмите `Get started`
- В файле `.gitlab-ci.yml` .gitlab-ci.yml удалите точки в начале заданий deploy firebase
- В файле `.gitlab-ci.yml` удалите задания для GitLab Distribution

## Настройка Firebase TestLab

- Инструкция - [Firebase TestLab](https://confluence.icerockdev.com/display/AD/Firebase+testlab)

## Настройка деплоя GitLab Distribution

- Получите токен из [Confluence](https://confluence.icerockdev.com/pages/viewpage.action?pageId=69437109)
- Выполните `./setup-gitlab-deploy.sh <gitlab_token> <gitlab_project_id> <deploy_token>`

## Настройка CI-конфигурации для iOS

- Выполните `./setup-ios-ci.sh <gitlab_token> <gitlab_project_id> <fastlane_password> <match_password>`

## Действия после настройки

1. Замените пакет org.example на пакет вашего приложения и перенесите файлы в нужные директории
2. Выполните TODO, указанные в проекте
   1. build-logic/src/main/kotlin/utils.kt
   2. build-logic/src/main/kotlin/android-app-convention.gradle.kts
   3. docs/DEVELOPMENT.md
   4. mpp-library/src/commonMain/kotlin/org/example/library/di/modules/NetworkModule.kt
   5. android/app/src/main/java/org/example/app/model/default***StateData.kt
   6. android/app/src/main/java/org/example/app/navigation/RootContainer.kt
   7. android/app/src/main/java/org/example/app/navigation/BottomBarItems.kt
   8. android/app/src/main/java/org/example/app/navigation/BottomBar.kt
