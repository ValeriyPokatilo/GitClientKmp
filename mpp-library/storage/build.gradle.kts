
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.android.library")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.multiplatformSettings)
    commonMainImplementation(libs.multiplatformSettings.noArg)
    commonMainImplementation(project(":mpp-library:utils"))
}

multiplatformResources {
    resourcesPackage = "app.xl.gitclientkmp"
}

android {
    namespace = "app.xl.gitclientkmp.storage"
}