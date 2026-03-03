#!/usr/bin/env bash

set -e
set -o pipefail

handle_error() {
    echo -e "\033[31mFast check failed, read logs above!\033[0m" >&2
    exit 1
}

trap handle_error ERR

# идем от самых быстрых задач к самым долгим. При чем запускаем только то, что наиболее показательно
# нам нет смысла делать сборки вообще всех таргетов айос например, достаточно одного.
compileGradle() {
  # check android release lint
  ./gradlew assembleDevRelease lintDevRelease
  # check ios release framework compilation too
  ./gradlew linkPodReleaseFrameworkIosArm64
}

main() {
  # запускаем быстрые проверки для начала
  ./fastcheck.sh

  # при проверке гредла проверятся все приложения и модули
  compileGradle
}

time main
