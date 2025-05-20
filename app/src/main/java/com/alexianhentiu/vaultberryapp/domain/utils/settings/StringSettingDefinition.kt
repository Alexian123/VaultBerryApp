package com.alexianhentiu.vaultberryapp.domain.utils.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

data class StringSettingDefinition(
    val name: String,
    override val defaultValue: String = "",
) : SettingDefinition<String> {

    override val key: Preferences.Key<String> = stringPreferencesKey(name)

    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }
}
