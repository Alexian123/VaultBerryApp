package com.alexianhentiu.vaultberryapp.domain.common

sealed class SettingDefinition<T> {

    abstract val key: String
    abstract val defaultValue: T

    data class BooleanDefinition(
        override val key: String,
        override val defaultValue: Boolean = false
    ) : SettingDefinition<Boolean>()

    data class StringDefinition(
        override val key: String,
        override val defaultValue: String = ""
    ) : SettingDefinition<String>()
}