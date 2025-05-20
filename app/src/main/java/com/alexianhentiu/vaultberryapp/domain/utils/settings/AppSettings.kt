package com.alexianhentiu.vaultberryapp.domain.utils.settings

object AppSettings {
    val USE_SYSTEM_THEME = BooleanSettingDefinition("use_system_theme", true)
    val DARK_THEME = BooleanSettingDefinition("dark_theme", false)
    val DEBUG_MODE = BooleanSettingDefinition("debug_mode", false)
    val SAVED_EMAIL = StringSettingDefinition("saved_email")
    val REMEMBER_EMAIL = BooleanSettingDefinition("remember_email", false)
}