import com.android.build.gradle.BaseExtension

plugins {
    id("kotlin-android")
    id("android-base-convention")
    id("org.jetbrains.kotlin.plugin.compose")
}

configure<BaseExtension> {
    buildFeatures.compose = true
}

dependencies {
    "implementation"(platform(libsCatalog.safeLibrary("koin-bom")))
    "implementation"(platform(libsCatalog.safeLibrary("compose-bom")))
    listOf(
        "compose-material3",
        "compose-materialIcons",
        "compose-foundation",
        "compose-foundationLayout",
        "compose-ui-preview",
        "compose-navigation",
        "koin-androidx-compose",
        "moko-mvvm-flow-compose",
    ).forEach { "implementation"(libsCatalog.safeLibrary(it)) }

    "debugImplementation"(libsCatalog.safeLibrary("compose-ui-tooling"))
}
