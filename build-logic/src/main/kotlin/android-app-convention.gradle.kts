plugins {
    id("base-convention")
    id("com.android.application")
    id("android-base-convention")
    id("kotlin-android")
}

base {
    archivesName.set("android-app")
}

android {
    flavorDimensions.addAll(listOf("server"))

    signingConfigs {
        create("release") {
            keyAlias = System.getenv("RELEASE_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
            storeFile = file("signing/release.jks")
            storePassword = System.getenv("RELEASE_STORE_PASSWORD")
        }
        // вместо использования стандартного debug создаем свой вариант, с расшаренным на всех
        // разработчиков ключём (в репозитории храним).
        // это нужно чтобы успешно отлаживать интеграции, требующие SHA ключа.
        // в стандартном debug у каждого разработчика собственный ключ подписи.
        create("sharedDebug") {
            keyAlias = "debug"
            keyPassword = "debugicerock"
            storeFile = file("signing/debug.jks")
            storePassword = "debugicerock"
        }
    }

    defaultConfig {
        multiDexEnabled = true
    }

    buildTypes {
        getByName("release") {
            val releaseConfig = signingConfigs.getByName("release")
            signingConfig = when {
                releaseConfig.keyAlias != null -> releaseConfig
                System.getenv("CI") == null -> {
                    logger.warn("used debug signing for release build!")
                    signingConfigs.getByName("sharedDebug")
                }

                else -> {
                    throw IllegalArgumentException("release signing not configured. Set RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD, RELEASE_STORE_PASSWORD environment variables.")
                }
            }

            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("sharedDebug")
            isDebuggable = true
            applicationIdSuffix = ".debug"
            ext.set("enableCrashlytics", false)
        }
    }

    productFlavors {
        create("dev") {
            dimension = "server"
            applicationIdSuffix = ".dev"

            // TODO заменить адрес на реальный адрес дев сервера
            val endpoint = "https://dev.localhost/"
            buildConfigField("String", "BASE_URL", "\"$endpoint\"")
        }

        create("stage") {
            dimension = "server"
            applicationIdSuffix = ".stage"

            // TODO заменить адрес на реальный адрес стейдж сервера
            val endpoint = "https://stage.localhost/"
            buildConfigField("String", "BASE_URL", "\"$endpoint\"")
        }

        create("prod") {
            dimension = "server"

            // TODO заменить адрес на реальный адрес прод сервера
            val endpoint = "https://localhost/"
            buildConfigField("String", "BASE_URL", "\"$endpoint\"")
        }
    }

    packaging {
        resources.excludes.add("META-INF/*.kotlin_module")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}
