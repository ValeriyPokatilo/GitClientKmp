plugins {
    id("multiplatform-library-convention")
}

// copy resources for android
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>()
    .matching { it.name.contains("UnitTest") }
    .configureEach {
        val testResourcesDir = File(this.project.projectDir, "src/commonTest/resources")
        val destinationDir: Provider<File> = this.destinationDirectory.asFile

        // To invalidate the cache when resources change, register them as input.
        this.inputs.dir(testResourcesDir)

        doLast {
            if (testResourcesDir.exists().not()) return@doLast
            testResourcesDir.copyRecursively(destinationDir.get(), overwrite = true)
        }
    }

// copy resources for ios
val copyIosArm64TestResources = tasks.register<Copy>("copyIosArm64TestResources") {
    from("src/commonTest/resources")
    into("build/bin/iosSimulatorArm64/debugTest/resources")
}

val copyIosX64TestResources = tasks.register<Copy>("copyIosX64TestResources") {
    from("src/commonTest/resources")
    into("build/bin/iosX64/debugTest/resources")
}

tasks.matching { it.name == "iosSimulatorArm64Test" }.configureEach {
    dependsOn(copyIosArm64TestResources)
}

tasks.matching { it.name == "iosX64Test" }.configureEach {
    dependsOn(copyIosX64TestResources)
}
