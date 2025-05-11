package com.alexianhentiu.vaultberryapp.domain.utils.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

class BooleanSettingDefinition(
    val name: String,
    override val defaultValue: Boolean = false,
) : SettingDefinition<Boolean> {

    override val key: Preferences.Key<Boolean> = booleanPreferencesKey(name)

    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }
}