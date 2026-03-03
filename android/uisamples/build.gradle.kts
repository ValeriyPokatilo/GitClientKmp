/*
 * Copyright 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

android.namespace = "org.example.android.uisamples"

dependencies {
    implementation(libs.compose.activity)
    implementation(libs.compose.material3)
    implementation(libs.napier)
    implementation(libs.moko.fields.flow)
    api(libs.kotlinxDateTime)
    api(projects.mppLibrary.utils)
}
