
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("dev.icerock.mobile.multiplatform-resources")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)
    commonMainImplementation(libs.moko.resources)
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
}
