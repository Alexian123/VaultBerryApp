package com.alexianhentiu.vaultberryapp.data.platform.datastore

import androidx.datastore.preferences.core.Preferences

sealed class DataStorePreference<T> {

    abstract val key: Preferences.Key<T>
    abstract val defaultValue: T

    data class BooleanPreference(
        override val key: Preferences.Key<Boolean>,
        override val defaultValue: Boolean = false
    ) : DataStorePreference<Boolean>()

    data class StringPreference(
        override val key: Preferences.Key<String>,
        override val defaultValue: String = ""
    ) : DataStorePreference<String>()
}