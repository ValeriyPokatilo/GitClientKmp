import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    alias(libs.plugins.buildKonfig)
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.android.library")
    kotlin("plugin.serialization")
}

buildkonfig {
    packageName = "org.example.app"

    val props = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    val imgbbApiKey = props.getProperty("IMGBB_API_KEY")
        ?: error("IMGBB_API_KEY missing")

    defaultConfigs {
        buildConfigField(
            STRING,
            "IMGBB_API_KEY",
            imgbbApiKey
        )
    }
}

dependencies {
    commonMainImplementation(platform(libs.koin.bom))
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.multiplatformSettings)
    commonMainImplementation(libs.multiplatformSettings.noArg)
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorClient.content.negotiation)
    commonMainImplementation(libs.ktorSerialization.kotlinx.json)
    commonMainImplementation(libs.moko.errors)
    commonMainImplementation(project(":mpp-library:entity"))
    commonMainImplementation(project(":mpp-library:res"))
    commonMainImplementation(project(":mpp-library:utils"))
    commonMainImplementation(project(":mpp-library:storage"))
}

multiplatformResources {
    resourcesPackage = "app.xl.gitclientkmp"
}

android {
    namespace = "app.xl.gitclientkmp.network"
}