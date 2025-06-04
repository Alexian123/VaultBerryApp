package com.alexianhentiu.vaultberryapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alexianhentiu.vaultberryapp.data.platform.datastore.DataStorePreference
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.common.SettingDefinition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override suspend fun <T> load(setting: SettingDefinition<T>): T {
        val preference = setting.toDataStorePreference()
        return dataStore.data.map { preferences ->
            preferences[preference.key] ?: preference.defaultValue
        }.first()
    }

    override suspend fun <T> save(setting: SettingDefinition<T>, value: T) {
        val preference = setting.toDataStorePreference()
        dataStore.edit { preferences ->
            preferences[preference.key] = value
        }
    }

    override fun <T> observe(setting: SettingDefinition<T>): Flow<T> {
        val preference = setting.toDataStorePreference()
        return dataStore.data.map { preferences ->
            preferences[preference.key] ?: preference.defaultValue
        }
    }

    /**
     * Converts a generic SettingDefinition to a DataStorePreference.
     * This function maps the String key from SettingDefinition to the appropriate
     * DataStore Preferences.Key type based on the specific subclass of SettingDefinition.
     */
    private fun <T> SettingDefinition<T>.toDataStorePreference(): DataStorePreference<T> {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is SettingDefinition.BooleanDefinition -> DataStorePreference.BooleanPreference(
                key = booleanPreferencesKey(this.key),
                defaultValue = this.defaultValue
            ) as DataStorePreference<T> // Cast is safe because T is Boolean here
            is SettingDefinition.StringDefinition -> DataStorePreference.StringPreference(
                key = stringPreferencesKey(this.key),
                defaultValue = this.defaultValue
            ) as DataStorePreference<T> // Cast is safe because T is String here
        }
    }
}