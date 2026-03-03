import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider

fun Project.getFeaturePackage(): String {
    val packageName: String = name.replace("-", ".")
    // TODO заменить на правильный пакет
    return "org.example.library.feature.$packageName"
}

internal val Project.libsCatalog: VersionCatalog
    get() = this.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal fun VersionCatalog.safeLibrary(alias: String): Provider<MinimalExternalModuleDependency> {
    try {
        return findLibrary(alias).get()
    } catch (_: Exception) {
        throw GradleException("library $alias not found in libs.versions.toml")
    }
}

internal fun VersionCatalog.safeBundle(alias: String): Provider<ExternalModuleDependencyBundle> {
    try {
        return findBundle(alias).get()
    } catch (_: Exception) {
        throw GradleException("bundle $alias not found in libs.versions.toml")
    }
}
