package com.alexianhentiu.vaultberryapp.domain.utils

class PasswordGenerator {

    fun generatePassword(
        length: Int,
        includeUppercase: Boolean,
        includeNumbers: Boolean,
        includeSpecialChars: Boolean,
        includeSpaces: Boolean
    ): String {
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

    // TODO: Improve password strength evaluation
    fun evaluatePasswordStrength(password: String): Int {
        var score = 0
        if (password.length >= 8) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any {!it.isLetterOrDigit() }) score++
        if (password.any { it.isWhitespace() }) score++
        return score
    }
}