package dev.icerock.tests.utils

/**
 * Функция для получения текста из файла в ресурсах (commonMain/resources например).
 *
 * Файлы могут быть в папках, путь формируется от директории resources.
 *
 * @param path путь до файла в папке resources
 *
 * @return строковое содержимое всего файла
 */
expect fun Any.readResourceText(path: String): String
