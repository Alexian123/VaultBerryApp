package com.alexianhentiu.vaultberryapp.domain.utils.security

class PasswordGenerator {

    fun generatePassword(
        length: Int,
        includeUppercase: Boolean,
        includeNumbers: Boolean,
        includeSpecialChars: Boolean,
        includeSpaces: Boolean
    ): String {
        if (length <= 0) throw IllegalArgumentException("Length must be greater than 0")
        val charPool = mutableListOf<Char>()
        charPool.addAll(('a'..'z').toList())
        if (includeUppercase) charPool.addAll(('A'..'Z').toList())
        if (includeNumbers) charPool.addAll(('0'..'9').toList())
        if (includeSpecialChars) charPool.addAll("!@#$%^&*()_+{}|;:,.<>?".toList())
        if (includeSpaces) charPool.add(' ')
        return (1..length)
            .map { charPool.random() }
            .joinToString("")
    }
}