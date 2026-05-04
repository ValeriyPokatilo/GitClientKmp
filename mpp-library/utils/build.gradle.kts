
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.android.library")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainApi(libs.kotlinxDateTime)
    commonMainImplementation(libs.moko.resources)
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.moko.units)
    commonMainImplementation(project(":mpp-library:res"))
}

multiplatformResources {
    resourcesPackage = "app.xl.gitclientkmp"
}

android {
    namespace = "app.xl.gitclientkmp.utils"
}