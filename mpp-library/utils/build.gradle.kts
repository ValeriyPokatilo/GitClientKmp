plugins {
    id("multiplatform-library-convention")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)
    commonMainApi(libs.moko.fields.flow)
    commonMainImplementation(libs.moko.resources)
    commonMainImplementation(libs.moko.errors)
    // убрать когда moko-errors обновится до новых ресурсов
    commonMainImplementation(libs.moko.parcelize)

    commonTestImplementation(projects.mppLibrary.testUtils)

    commonMainImplementation(libs.kotlinxDateTime)
}

android.namespace = "org.example.library.utils"
