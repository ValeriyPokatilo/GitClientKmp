package org.example.library.di.modules

import org.example.library.model.TokenStorage
import org.example.library.storage.KeyValueStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

internal val keyValueStorageModule = module {
    singleOf(::KeyValueStorage) binds arrayOf(TokenStorage::class, KeyValueStorage::class)
}
