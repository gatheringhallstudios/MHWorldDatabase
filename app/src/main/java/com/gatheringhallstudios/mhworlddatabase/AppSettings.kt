package com.gatheringhallstudios.mhworlddatabase

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * A static class used to manage shared preferences and application settings.
 * Must be initialized via the AppSettings.bindApplication() function.
 */
class AppSettings {
    companion object {
        @JvmStatic
        val SETTINGS_FILE_NAME = "MHWDatabase.settings"

        private var application : Application? = null
        private val validLanguages = mutableListOf<String>()

        @JvmStatic
        fun bindApplication(app : Application) {
            application = app
        }

        /**
         * Binds the list of valid languages. Must be a list of locale ids.
         */
        @JvmStatic
        fun bindValidLanguages(languages: List<String>) {
            validLanguages.addAll(languages)
        }

        private val sharedPreferences : SharedPreferences
            get() {
                if (application == null) {
                    throw UninitializedPropertyAccessException("Application not initialized")
                }
                return application!!.applicationContext.getSharedPreferences(SETTINGS_FILE_NAME, Context.MODE_PRIVATE)
            }

        /**
         * Returns the configured data locale. This is the locale setting the user has set, which may be the empty string.
         * When translating, use dataLocale instead, which returns the resolved data locale.
         */
        @JvmStatic
        val configuredDataLocale: String
            get() = sharedPreferences.getString(PROP_DATA_LOCALE, "") ?: ""

        /**
         * Returns the data locale that would be resolved
         */
        @JvmStatic
        val defaultDataLocale: String
            get() {
                val locale = Locale.getDefault().language
                return when (locale) {
                    in validLanguages -> locale
                    else -> "en"
                }
            }

        /**
         * Returns the resolved data locale. Defaults to the system language,
         * or english if invalid.
         */
        @JvmStatic
        val dataLocale : String
            get() {
                val pref = configuredDataLocale
                if (pref.isNotBlank()) {
                    return pref
                }

                return defaultDataLocale
            }

        /**
         * Settings key for data locale language
         */
        val PROP_DATA_LOCALE = "DATA_LOCALE"

    }
}