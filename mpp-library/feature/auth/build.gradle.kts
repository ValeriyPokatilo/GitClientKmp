
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)
    commonMainImplementation(libs.moko.resources)
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.moko.errors)
    commonMainImplementation(project(":mpp-library:feature:common"))
}
