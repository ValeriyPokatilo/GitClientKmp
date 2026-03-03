package org.example.sample.di

import org.example.sample.ExampleViewModel
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val featureExampleModule: Module = module {
    factoryOf(::ExampleViewModel)
}

fun Koin.getExampleViewModel(params: ExampleViewModel.Params): ExampleViewModel {
    return get<ExampleViewModel> { parametersOf(params) }
}
