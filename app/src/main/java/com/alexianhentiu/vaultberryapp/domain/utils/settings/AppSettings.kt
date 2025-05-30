package com.alexianhentiu.vaultberryapp.domain.utils.settings

object AppSettings {

    val USE_SYSTEM_THEME = SettingDefinition.BooleanDefinition("use_system_theme", false)

    val DARK_THEME = SettingDefinition.BooleanDefinition("dark_theme")

    val DEBUG_MODE = SettingDefinition.BooleanDefinition("debug_mode")

    val SAVED_EMAIL = SettingDefinition.StringDefinition("saved_email")

    val REMEMBER_EMAIL = SettingDefinition.BooleanDefinition("remember_email")

}