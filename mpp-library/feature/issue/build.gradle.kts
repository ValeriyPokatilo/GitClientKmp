
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
    commonMainApi(libs.moko.fields.core)
    commonMainApi(libs.moko.fields.livedata)
    commonMainApi(libs.moko.paging)
    androidMainImplementation(libs.koin.android)
    commonMainImplementation(project(":mpp-library:entity"))
    commonMainImplementation(project(":mpp-library:network"))
    commonMainImplementation(project(":mpp-library:utils"))
    commonMainImplementation(project(":mpp-library:res"))
}
