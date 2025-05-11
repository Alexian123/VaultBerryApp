package com.alexianhentiu.vaultberryapp.domain.utils.settings

import androidx.datastore.preferences.core.Preferences

interface SettingDefinition<T> {
    val key: Preferences.Key<T>
    val defaultValue: T
}