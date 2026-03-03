plugins {
    id("multiplatform-library-convention")
}

dependencies {
    // every module linked with DI by Koin
    commonMainImplementation(platform(libsCatalog.safeLibrary("koin-bom")))
    commonMainImplementation(libsCatalog.safeLibrary("koin-core"))
}
