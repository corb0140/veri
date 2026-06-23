package com.example.biometricvault

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object ThemePreferenceManager {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_MODE = "night_mode"

    fun saveMode(context: Context, mode: Int) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit { putInt(KEY_MODE, mode) }
    }

    fun getSavedMode(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun applyMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(getSavedMode(context))
    }
}