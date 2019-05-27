package com.cyrilfind.kodo.settings

import android.os.Bundle
import android.view.Menu
import androidx.preference.PreferenceFragmentCompat
import com.cyrilfind.kodo.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}