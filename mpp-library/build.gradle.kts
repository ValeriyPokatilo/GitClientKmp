/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    id("multiplatform-library-convention")
    id("detekt-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.mobile.multiplatform-network-generator")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("kotlinx-serialization")
    id("skie-convention")
}

val projectModules = listOf(
    projects.mppLibrary.feature.auth,
    projects.mppLibrary.feature.repo,
    projects.mppLibrary.network,
    projects.mppLibrary.utils,
    projects.mppLibrary.entity,
    projects.mppLibrary.storage
)

kotlin {
    cocoapods {
        authors = "IceRock Development"

        version = "1.0"
        name = "MultiPlatformLibrary"
        summary = "Shared code between iOS and Android"
        homepage = "Link to a Kotlin/Native module homepage"

        ios.deploymentTarget = "15.0"

        listOf("dev", "stage", "prod").forEach { schemeName ->
            xcodeConfigurationToNativeBuildType["$schemeName-debug"] = NativeBuildType.DEBUG
            xcodeConfigurationToNativeBuildType["$schemeName-release"] = NativeBuildType.RELEASE
        }

        framework {
            baseName = "MultiPlatformLibrary"

            projectModules.forEach {
                export(it)
            }
            export(libs.multiplatformSettings)
            export(libs.napier)
            export(libs.moko.resources)
            export(libs.moko.graphics)
            export(libs.moko.mvvm.core)
            export(libs.moko.mvvm.flow)
            export(libs.moko.mvvm.state)
            export(libs.moko.fields.core)
            export(libs.moko.errors)
            export(libs.moko.crashReporting.core)
            export(libs.moko.units)
        }
    }
}

dependencies {
    projectModules.forEach {
        commonMainApi(it)
    }

    commonMainImplementation(libs.coroutines)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorClientLogging)
    commonMainImplementation(libs.ktorClientAuth)


    androidMainImplementation(libs.multidex)
    androidMainImplementation(libs.lifecycleViewModel)

    commonMainApi(libs.moko.parcelize) // Remove after update moko libs
    commonMainApi(libs.multiplatformSettings)
    commonMainImplementation(libs.multiplatformSettings.serialization)
    commonMainApi(libs.napier)
    commonMainApi(libs.moko.resources)
    commonMainApi(libs.moko.mvvm.core)
    commonMainApi(libs.moko.mvvm.state)
    commonMainApi(libs.moko.mvvm.flow)
    commonMainApi(libs.moko.fields.core)
    commonMainApi(libs.moko.errors)
    commonMainApi(libs.moko.crashReporting.core)
    commonMainApi(libs.moko.crashReporting.napier)
    commonMainImplementation(libs.moko.network)
    commonMainImplementation(libs.moko.network.errors)
    commonMainImplementation(libs.moko.network.engine)
    commonMainImplementation(libs.moko.units)

    //Koin DI
    commonMainApi(platform(libs.koin.bom))
    commonMainApi(libs.koin.core)
    androidMainApi(libs.koin.android)
    androidMainApi(libs.koin.android.compose)
    commonMainImplementation(libs.koin.core.coroutines)
}

multiplatformResources {
    resourcesPackage = "org.example.library"
    resourcesClassName = "AppRes"
}

mokoNetwork {
    spec("serverApi") {
        inputSpec = file("src/api/openapi.yml")
        isInternal = true
    }
}

android {
    namespace = "org.example.library"
}
