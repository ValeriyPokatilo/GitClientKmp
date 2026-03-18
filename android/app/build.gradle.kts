/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    // TODO: - uncomment to use Firebase
    // id("com.google.gms.google-services")
    id("android-app-convention")
    id("com.google.firebase.crashlytics")
    id("android-compose-convention")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "org.example.app"

    defaultConfig {
        applicationId = "app.xl.gitclientkmp"

        versionCode = Integer.parseInt(project.property("VERSION_CODE") as String)
        versionName = project.property("VERSION_NAME") as String
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.lifecycleRuntime)
    implementation(libs.splashScreen)

    //Navigation
    implementation(libs.navigationComponent)
    implementation(libs.navigationUIComponent)

    //Compose
    implementation(libs.compose.activity)
    implementation(libs.coil.compose)
    implementation(libs.moko.mvvm.flow.compose)
    implementation(libs.moko.resources.compose)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.moko.crashReporting.crashlytics)

    implementation(projects.mppLibrary)
    implementation(projects.android.utils)
    implementation(projects.android.uikit)

    implementation(libs.markwon)
    implementation(libs.moko.units)
}
