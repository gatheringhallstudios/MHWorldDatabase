package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.squareup.leakcanary.LeakCanary

class MhwApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize application settings
        // todo: if there are issues, create an Application subclass and bind there
        AppSettings.bindApplication(this)
        AssetLoader.bindApplication(this)

        // Setup Leak Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }
}