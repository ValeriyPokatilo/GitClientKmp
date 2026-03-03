package dev.icerock.tests.utils

import dev.icerock.moko.resources.desc.StringDesc

actual fun StringDesc.toLocalizedString(): String {
    return this.localized()
}
