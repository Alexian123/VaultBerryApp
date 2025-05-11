package com.alexianhentiu.vaultberryapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "com.alexianhentiu.vaultberryapp.user_settings"
)