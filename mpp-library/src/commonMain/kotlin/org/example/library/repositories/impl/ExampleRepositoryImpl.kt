package org.example.library.repositories.impl

import org.example.library.storage.KeyValueStorage
import org.example.sample.model.ExampleRepository

class ExampleRepositoryImpl internal constructor(
    private val keyValueStorage: KeyValueStorage,
) : ExampleRepository {

    override fun saveInKeyValueStorage(value: String) {
        keyValueStorage.language = value
    }
}
