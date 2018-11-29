package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * A static class used to manage shared preferences and application settings.
 * Must be initialized via the AppSettings.bindApplication() function.
 */
class AppSettings {
    companion object {
        @JvmStatic
        val SETTINGS_FILE_NAME = "MHWDatabase.settings"

        private var application : Application? = null

        @JvmStatic
        fun bindApplication(app : Application) {
            application = app
        }

        private val sharedPreferences : SharedPreferences
            get() {
                if (application == null) {
                    throw UninitializedPropertyAccessException("Application not initialized")
                }
                return application!!.applicationContext.getSharedPreferences(SETTINGS_FILE_NAME, Context.MODE_PRIVATE)
            }

        @JvmStatic
        val dataLocale : String
            get() = sharedPreferences.getString(PROP_DATA_LOCALE, "en")

        /**
         * Settings key for data locale language
         */
        val PROP_DATA_LOCALE = "DATA_LOCALE"

    }
}