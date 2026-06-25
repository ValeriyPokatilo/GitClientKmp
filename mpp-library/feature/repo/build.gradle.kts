
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.moko.errors)
    androidMainImplementation(libs.koin.android)
    commonMainImplementation(project(":mpp-library:network"))
    commonMainImplementation(project(":mpp-library:utils"))
    commonMainImplementation(project(":mpp-library:entity"))
}
