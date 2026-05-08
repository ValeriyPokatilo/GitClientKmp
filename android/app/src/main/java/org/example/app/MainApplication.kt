/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.app

import android.app.Application
import app.xl.gitclientkmp.di.issueAndroidModule
import app.xl.gitclientkmp.di.repoAndroidModule
import app.xl.gitclientkmp.di.utilsAndroidModule
import app.xl.gitclientkmp.domain.error.Configurator
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.icerock.moko.crashreporting.crashlytics.CrashlyticsLogger
import org.example.app.utils.LogcatAntilog
import org.example.library.di.startDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = BuildConfig.DEBUG.not()

        val antilog: LogcatAntilog? = if (BuildConfig.DEBUG) {
            LogcatAntilog()
        } else {
            null
        }

        startDI(
            antilog = antilog,
            exceptionLogger = CrashlyticsLogger()
        ) {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }

            androidContext(this@MainApplication)

            modules(repoAndroidModule, issueAndroidModule, utilsAndroidModule)
        }

        Configurator.init()
    }
}
