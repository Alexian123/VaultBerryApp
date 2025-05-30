package com.alexianhentiu.vaultberryapp.presentation.utils.containers

data class PasswordGenOptions(
    val length: Int = 16,
    val includeUppercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSpecialChars: Boolean = true,
    val includeSpaces: Boolean = false
)
