package com.alexianhentiu.vaultberryapp.domain.utils.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsManager(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun <T> load(definition: SettingDefinition<T>): T {
        return dataStore.data
            .map { preferences ->
                preferences[definition.key] ?: definition.defaultValue
            }
            .first()
    }

    suspend fun <T> save(definition: SettingDefinition<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[definition.key] = value
        }
    }

    fun <T> observe(definition: SettingDefinition<T>): Flow<T> {
        return dataStore.data
            .map { preferences ->
                preferences[definition.key] ?: definition.defaultValue
            }
    }
}