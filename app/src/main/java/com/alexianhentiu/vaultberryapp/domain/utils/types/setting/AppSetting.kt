package com.alexianhentiu.vaultberryapp.domain.utils.types.setting

import androidx.datastore.preferences.core.Preferences

interface AppSetting<T> {
    val key: Preferences.Key<T>
    val defaultValue: T
}