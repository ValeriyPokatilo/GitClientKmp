/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "mobile-moko-boilerplate"

includeBuild("build-logic")

include(":android:app")
include(":android:utils")
include(":android:uisamples")
include(":android:uikit")
include(":mpp-library")
include(":mpp-library:utils")
include(":mpp-library:test-utils")
include(":mpp-library:feature:example")
include(":mpp-library:feature:auth")
include(":mpp-library:feature:repo")
include(":mpp-library:feature:common")
