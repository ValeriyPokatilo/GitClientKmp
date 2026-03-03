/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

android.namespace = "org.example.android.uikit"

dependencies {
    implementation(libs.compose.activity)
    implementation(libs.moko.mvvm.state)
    implementation(libs.moko.fields.flow)
    implementation(libs.coil.compose)
    implementation(libs.moko.mvvm.flow.compose)
    api(libs.moko.resources.compose)
    implementation(projects.android.utils)
}
