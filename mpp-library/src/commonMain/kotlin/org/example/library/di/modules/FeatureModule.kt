package org.example.library.di.modules

import org.example.sample.di.featureExampleModule
import org.koin.dsl.module

internal val featuresModules = module {
    includes(featureExampleModule)
}
