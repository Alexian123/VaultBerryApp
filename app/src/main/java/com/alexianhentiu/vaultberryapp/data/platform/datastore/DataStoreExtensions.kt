package com.alexianhentiu.vaultberryapp.data.platform.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.appSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_settings"
)

val Context.authCredentialsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_credentials"
)

