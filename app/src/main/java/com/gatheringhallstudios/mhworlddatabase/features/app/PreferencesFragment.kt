package com.gatheringhallstudios.mhworlddatabase.features.app

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.MainActivity
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.entities.Language

/**
 * Fragment used to display app preferences
 */
class PreferencesFragment : PreferenceFragmentCompat() {
    private val restartListener = RestartOnLocaleChangeListener(this)

    // add listener on resume
    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(restartListener)
    }

    // remove listener on pause
    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(restartListener)
        super.onPause()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = AppSettings.SETTINGS_FILE_NAME

        setPreferencesFromResource(R.xml.preferences, rootKey)
        initDataLanguages()
    }

    private fun initDataLanguages() {
        val localePref = findPreference(AppSettings.PROP_DATA_LOCALE) as ListPreference

        // Get the list of languages. Add a "default" language to the front
        val defaultLanguage = Language("", getString(R.string.preference_language_default))
        val languages = listOf(defaultLanguage) + MHWDatabase.getDatabase(context).languages
        val languageCodes = languages.map { it.id }
        val languageNames = languages.map { it.name }

        localePref.entryValues = languageCodes.toTypedArray()
        localePref.entries = languageNames.toTypedArray()
        localePref.value = AppSettings.configuredDataLocale // ensure a value is set
    }

    /**
     * Internal class to restart the app if the locale changes
     */
    class RestartOnLocaleChangeListener(val fragment: Fragment) : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key == AppSettings.PROP_DATA_LOCALE) {
                (fragment.activity as? MainActivity)?.restartApp()
            }
        }
    }
}