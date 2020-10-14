package com.cyrilfind.kodo.ui.settings

import android.os.Bundle
import android.view.Menu
import androidx.core.content.edit
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.cyrilfind.kodo.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setHasOptionsMenu(true)
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString("SHARED_PREF_TOKEN_KEY", "fetchedToken")
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}