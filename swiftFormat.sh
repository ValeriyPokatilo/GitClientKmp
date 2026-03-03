#!/usr/bin/env bash

set -e
set -o pipefail

handle_error() {
    echo -e "\033[31mFast check failed, read logs above!\033[0m" >&2
    exit 1
}

trap handle_error ERR

swiftFormat() {
  # Проверяем наличие переданного аргумента для директории проекта
  if [ -z "$1" ]; then
      echo "No project provided. Please specify a project."
      return 1
  fi
  PROJECT_ROOT=$1

  "Pods/SwiftFormat/CommandLineTool/swiftformat" \
    --config "icerock.swiftformat" \
    "${PROJECT_ROOT}/src"
}

main() {
  # дальше всё делаем в папке ios
  cd ios
  # проверяем swift lint
  swiftFormat App
  swiftFormat DesignSystem
}

time main
