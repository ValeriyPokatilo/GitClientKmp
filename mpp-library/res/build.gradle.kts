
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.android.library")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainImplementation(libs.moko.resources)
}

multiplatformResources {
    resourcesPackage = "app.xl.gitclientkmp"
}

android {
    namespace = "app.xl.gitclientkmp.res"
}