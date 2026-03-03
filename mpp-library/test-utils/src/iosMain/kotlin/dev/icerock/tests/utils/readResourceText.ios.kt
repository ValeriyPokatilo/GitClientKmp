package dev.icerock.tests.utils

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile
import kotlin.test.assertNotNull

actual fun Any.readResourceText(path: String): String {
    val pathWithoutExtension: String = path.substringBeforeLast(".")
    val extension = path.substringAfterLast(".")
    val filePath: String? = NSBundle.mainBundle
        .pathForResource("resources/$pathWithoutExtension", extension)

    assertNotNull(
        actual = filePath,
        message = buildString {
            append("can't find ")
            append(NSBundle.mainBundle.resourcePath)
            append(" file on ")
            append(path)
            append(" pathWithoutExtension = ")
            append(pathWithoutExtension)
            append(" extension = ")
            append(extension)
            append(" filePath=[")
            append(filePath)
            append("]")
        }
    )

    return NSString.stringWithContentsOfFile(filePath) as String
}
