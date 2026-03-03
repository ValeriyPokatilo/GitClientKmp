plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()

    gradlePluginPortal()
}

dependencies {
    api(libs.moko.multiplatformPlugin)
    api(libs.moko.resourcesGeneratorGradle)
    api(libs.kotlinGradlePlugin)
    api(libs.androidGradlePlugin)
    api(libs.detektGradlePlugin)
    api(libs.skieGradle)
    api(libs.composeGradlePlugin)
}
