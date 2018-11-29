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

        // todo: implement "default" language. Add a fake language entry in the beginning of the list
        val languages = MHWDatabase.getDatabase(context).languages
        val languageCodes = languages.map { it.id }
        val languageNames = languages.map { it.name }

        localePref.entryValues = languageCodes.toTypedArray()
        localePref.entries = languageNames.toTypedArray()
        localePref.value = AppSettings.dataLocale // ensure a value is set
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