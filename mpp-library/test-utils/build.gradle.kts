plugins {
    id("multiplatform-library-convention")
}

dependencies {
    commonMainApi(libs.moko.resources)
    commonMainApi(libs.moko.test.core)
    commonMainApi(libs.moko.test.robolectric)
    commonMainApi(libs.moko.mvvm.test)
    commonMainApi(libs.ktorClientMock)

    commonMainApi(libs.multiplatformSettings)
    commonMainApi(libs.multiplatformSettings.test)

    commonMainImplementation(platform(libs.koin.bom))
    commonMainApi(libs.koin.test)

    commonMainImplementation(libs.kotlinSerialization)

    androidMainApi(libs.androidx.test.core)
    androidMainApi(libs.robolectric)
    androidMainApi(kotlin("test-junit"))
    androidMainApi(libs.junit)
}

android.namespace = "org.example.library.test_utils"
