/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

android.namespace = "org.example.android.utils"

dependencies {
    implementation(libs.compose.activity)
    implementation(libs.napier)
    implementation(libs.moko.fields.flow)
    api(libs.kotlinxDateTime)
    api(libs.kotlinxImmutable)
    api(projects.mppLibrary.utils)
}
