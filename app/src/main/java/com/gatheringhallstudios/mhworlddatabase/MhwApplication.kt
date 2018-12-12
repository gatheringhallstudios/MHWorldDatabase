package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import android.os.Build
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.squareup.leakcanary.LeakCanary

class MhwApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize application settings
        // todo: if there are issues, create an Application subclass and bind there
        AppSettings.bindApplication(this)
        AssetLoader.bindApplication(this)

        // Bind supported languages to app settings
        val languages = MHWDatabase.getDatabase(this).languages.map { it.id }
        AppSettings.bindValidLanguages(languages)

        // Setup Leak Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Bug in P causes excessive leaks, just disable for now
            LeakCanary.install(this)
        }
    }
}