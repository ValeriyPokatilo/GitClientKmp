import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    config.setFrom(rootProject.file("config/detekt.yml"))
    buildUponDefaultConfig = true
}

val detekt = tasks.register("detektWithoutTests") {
    dependsOn(tasks.withType<Detekt>().matching { it.name.contains("Test").not() })
}
tasks.matching { it.name == "check" }.configureEach {
    dependsOn(detekt)
}

tasks.withType<Detekt>().configureEach {
    mustRunAfter(
        tasks.matching {
            it.name.lowercase().contains("generate")
        }
    )

    // exclude build directory from scan
    // /build/ for linux/macos, \build\ for windows
    val buildDir = File.separator + "build" + File.separator
    exclude { element ->
        element.file.path.contains(buildDir)
    }
}

dependencies {
    detektPlugins(libsCatalog.safeLibrary("detekt-formatting"))
}
