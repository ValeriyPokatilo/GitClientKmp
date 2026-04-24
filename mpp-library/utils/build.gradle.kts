plugins {
    id("multiplatform-library-convention")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)
    commonMainApi(libs.moko.fields.flow)
    commonMainImplementation(libs.moko.resources)
    commonMainImplementation(libs.moko.errors)
    commonMainImplementation(libs.moko.parcelize)
    commonMainImplementation(libs.kotlinxDateTime)
}

android.namespace = "org.example.library.utils"
