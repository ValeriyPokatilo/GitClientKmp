plugins {
    id("base-convention")
    id("com.android.library")
    id("android-base-convention")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        applyDefaultHierarchyTemplate()
    }
}

dependencies {
    // all modules need this dependency
    commonMainImplementation(libsCatalog.safeLibrary("coroutines"))
    commonMainImplementation(libsCatalog.safeLibrary("napier"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        // suppress expect/actual warning (usage in moko-resources)
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
