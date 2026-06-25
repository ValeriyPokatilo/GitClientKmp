package org.example.app.utils

import app.xl.gitclientkmp.Repository
import dev.icerock.moko.units.UnitItem
import org.example.app.presentation.RepoUnitItem

fun Repository.toUnitItem(
    onClick: (Repository) -> Unit
): UnitItem {
    return RepoUnitItem(
        itemId = id.hashCode().toLong(),
        repository = this,
        onClick = onClick
    )
}
