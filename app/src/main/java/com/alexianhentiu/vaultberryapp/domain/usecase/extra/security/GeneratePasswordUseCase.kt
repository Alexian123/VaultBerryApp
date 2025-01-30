package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.utils.PasswordGenerator

class GeneratePasswordUseCase(private val passwordGenerator: PasswordGenerator) {
    operator fun invoke(
        length: Int = 8,
        includeUppercase: Boolean = true,
        includeNumbers: Boolean = true,
        includeSpecialChars: Boolean = true,
        includeSpaces: Boolean = false
    ): String {
        return passwordGenerator.generatePassword(
            length,
            includeUppercase,
            includeNumbers,
            includeSpecialChars,
            includeSpaces
        )
    }
}