package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.gatheringhallstudios.mhworlddatabase.assets.AssetLoader
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.features.bookmarks.BookmarksFeature

class MhwApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize application settings
        // todo: if there are issues, create an Application subclass and bind there
        AppSettings.bindApplication(this)
        AssetLoader.bindApplication(this)
        BookmarksFeature.bindApplication(this)

        // Bind supported languages to app settings
        val languages = MHWDatabase.getDatabase(this).languages.map { it.id }
        AppSettings.bindValidLanguages(languages)

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}