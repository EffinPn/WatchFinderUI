package com.example.watchfinder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


private const val PREFERENCES_NAME = "my_app_preferences"


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

object AppPreferenceKeys {
    val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode")
}