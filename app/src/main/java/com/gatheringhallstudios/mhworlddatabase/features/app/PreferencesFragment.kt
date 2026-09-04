package com.gatheringhallstudios.mhworlddatabase.features.app

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.gatheringhallstudios.mhworlddatabase.AppSettings
import com.gatheringhallstudios.mhworlddatabase.MainActivity
import com.gatheringhallstudios.mhworlddatabase.R
import com.gatheringhallstudios.mhworlddatabase.data.MHWDatabase
import com.gatheringhallstudios.mhworlddatabase.data.entities.Language
import java.util.Locale

/**
 * Fragment used to display app preferences
 */
class PreferencesFragment : PreferenceFragmentCompat() {
    private val restartListener = RestartOnLocaleChangeListener(this)

    // add listener on resume
    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(restartListener)
    }

    // remove listener on pause
    override fun onPause() {
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(restartListener)
        super.onPause()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = AppSettings.SETTINGS_FILE_NAME

        setPreferencesFromResource(R.xml.preferences, rootKey)
        initAppLanguages()
        initDataLanguages()
    }

    /**
     * Populates the UI language picker and hands changes to the per-app locale API.
     *
     * This preference is deliberately not persisted: [AppCompatDelegate] owns the selection
     * (delegating to the platform on API 33+, and to its own store below that), so mirroring
     * it into SharedPreferences would leave a second copy to drift. Setting the locale
     * recreates the activity, so no explicit restart is needed here.
     */
    private fun initAppLanguages() {
        val localePref = findPreference(AppSettings.PROP_APP_LOCALE) as ListPreference? ?: return

        val tags = resources.getStringArray(R.array.app_locales)
        // Show each language in its own language, the way system pickers do.
        val names = tags.map { tag ->
            val locale = Locale.forLanguageTag(tag)
            locale.getDisplayName(locale).replaceFirstChar { it.uppercase(locale) }
        }

        localePref.entryValues = arrayOf("") + tags
        localePref.entries =
                arrayOf(getString(R.string.preference_app_language_default)) + names
        localePref.isPersistent = false
        localePref.value = resolveCurrentAppLocale(tags)

        localePref.setOnPreferenceChangeListener { _, newValue ->
            val tag = newValue as? String ?: ""
            AppCompatDelegate.setApplicationLocales(when {
                tag.isEmpty() -> LocaleListCompat.getEmptyLocaleList()
                else -> LocaleListCompat.forLanguageTags(tag)
            })
            true
        }
    }

    /**
     * Maps the active locale onto one of [tags], or "" for "follow the system".
     *
     * The stored locale does not have to be one of ours verbatim -- the system picker can set
     * a region we don't ship (de-DE), so fall back to matching on language alone.
     */
    private fun resolveCurrentAppLocale(tags: Array<String>): String {
        val current = AppCompatDelegate.getApplicationLocales()
        val locale = if (current.isEmpty) null else current[0]
        return when (locale) {
            null -> ""
            else -> tags.firstOrNull { it.equals(locale.toLanguageTag(), ignoreCase = true) }
                    ?: tags.firstOrNull { Locale.forLanguageTag(it).language == locale.language }
                    ?: ""
        }
    }

    private fun initDataLanguages() {
        val localePref = findPreference(AppSettings.PROP_DATA_LOCALE) as ListPreference?

        // Get the list of languages. Add a "default" language to the front
        val defaultLanguage = Language("", getString(R.string.preference_language_default))
        val languages = listOf(defaultLanguage) + MHWDatabase.getDatabase(context).languages
        val languageCodes = languages.map { it.id }
        val languageNames = languages.map { it.name }

        localePref?.entryValues = languageCodes.toTypedArray()
        localePref?.entries = languageNames.toTypedArray()
        localePref?.value = AppSettings.configuredDataLocale // ensure a value is set
    }

    /**
     * Internal class to restart the app if the locale changes
     */
    class RestartOnLocaleChangeListener(val fragment: androidx.fragment.app.Fragment) : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key == AppSettings.PROP_DATA_LOCALE) {
                (fragment.activity as? MainActivity)?.restartApp()
            }
        }
    }
}