
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.android.library")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)
    commonMainImplementation(libs.moko.resources)
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.multiplatformSettings)
    commonMainImplementation(libs.multiplatformSettings.noArg)
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorClient.content.negotiation)
    commonMainImplementation(libs.ktorSerialization.kotlinx.json)
    commonMainImplementation(libs.moko.units)
    commonMainImplementation(libs.moko.errors)
}

multiplatformResources {
    resourcesPackage = "app.xl.gitclientkmp"
}