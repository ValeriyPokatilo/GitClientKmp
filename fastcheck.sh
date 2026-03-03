#!/usr/bin/env bash

set -e
set -o pipefail

handle_error() {
    echo -e "\033[31mFast check failed, read logs above!\033[0m" >&2
    exit 1
}

trap handle_error ERR

# делает установку подов, а в случае ошибки неактуальных репов - делает реастарт с repo-update
podInstallUpdate() {
  (pod install) || {
    if [ $? -eq 31 ]; then
      echo "pod install failed with code 31. Retrying with --repo-update..."
      (pod install --repo-update)
    else
      exit $?
    fi
  }
}

# делает установку подов, создавая dummyFramework если надо, а также вызывая --repo-update если надо
podInstall() {
  if pod &> /dev/null; then
    echo "CocoaPods is installed. Prepare pods."
    (podInstallUpdate) || {
        if [ $? -eq 1 ]; then
          echo "pod install failed, retry after generateDummyFramework"
          (cd .. && ./gradlew generateDummyFramework)
          podInstallUpdate
        else
          echo "pod install failed with $? that unrecoverable"
          handle_error
        fi
      }
  else
    echo "CocoaPods is not installed. Skip pods setup."
  fi
}

# идем от самых быстрых задач к самым долгим. При чем запускаем только то, что наиболее показательно
# нам нет смысла делать сборки вообще всех таргетов айос например, достаточно одного.
compileGradle() {
  # info
  ./gradlew --version
  # detekt
  ./gradlew detektWithoutTests
  # faster way - android compilation and tests
  ./gradlew assembleDevDebug testDebugUnitTest
  # check ios compilation and tests - передаем обе платформы для теста чтобы и на arm и на x
  # компах тест выполнился. неподдерживаемая платформа просто пропустит задачу
  ./gradlew compileKotlinIosX64 iosX64Test iosSimulatorArm64Test
  # check ios framework compilation
  ./gradlew linkPodDebugFrameworkIosArm64
}

swiftFormatLint() {
  # Проверяем наличие переданного аргумента для директории проекта
  if [ -z "$1" ]; then
      echo "No project provided. Please specify a project."
      return 1
  fi
  PROJECT_ROOT=$1

  "Pods/SwiftFormat/CommandLineTool/swiftformat" \
    --config "icerock.swiftformat" \
    "${PROJECT_ROOT}/src" \
    --lint
}

compileXcode() {
  if xcode-select -p &> /dev/null; then
      echo "Xcode is installed. Try to check iOS compilation"

      ARCH=$(uname -m)

      # Проверяем наличие переданного аргумента для схемы
      if [ -z "$1" ]; then
          echo "No scheme provided. Please specify a scheme."
          return 1
      fi
      SCHEME=$1

      # собираем нужный нужную нам схему
      xcodebuild -scheme "$SCHEME" -workspace ios.xcworkspace \
          -sdk iphonesimulator -arch "$ARCH" \
          build \
          CODE_SIGNING_REQUIRED=NO CODE_SIGNING_ALLOWED=NO \
          | xcpretty

      echo "Xcode compilation success"
  else
    echo "Xcode not installed. Skip iOS compilation check"
  fi
}

main() {
  # при проверке гредла проверятся все приложения и модули
  compileGradle
  # дальше всё делаем в папке ios
  cd ios
  # устанавливаем поды. с обработкой кейсов ошибки
  podInstall
  # проверяем swift lint
  swiftFormatLint App
  swiftFormatLint DesignSystem
  # проверяем сборку приложения айос
  compileXcode ios-app-dev
}

time main
