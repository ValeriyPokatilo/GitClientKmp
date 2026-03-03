import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

configure<BaseExtension> {
    buildFeatures.buildConfig = true

    compileSdkVersion(35)

    defaultConfig {
        minSdk = 26
        targetSdk = 35
    }
}

plugins.withType<KotlinBasePlugin> {
    configure<org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension> {
        jvmToolchain(17)
    }
}
