package com.alexianhentiu.vaultberryapp.domain.utils.types.setting

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

class BooleanSetting(
    val name: String,
    override val defaultValue: Boolean = false,
) : AppSetting<Boolean>{

    override val key: Preferences.Key<Boolean> = booleanPreferencesKey(name)

    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }
}