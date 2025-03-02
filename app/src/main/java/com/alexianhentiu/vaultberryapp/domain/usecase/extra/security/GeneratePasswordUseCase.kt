package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.PasswordGenerator

class GeneratePasswordUseCase(private val passwordGenerator: PasswordGenerator) {
    operator fun invoke(
        length: Int = 8,
        includeUppercase: Boolean = true,
        includeNumbers: Boolean = true,
        includeSpecialChars: Boolean = true,
        includeSpaces: Boolean = false
    ): ActionResult<String> {
        try {
            val password = passwordGenerator.generatePassword(
                length,
                includeUppercase,
                includeNumbers,
                includeSpecialChars,
                includeSpaces
            )
            return ActionResult.Success(password)
        } catch (e: Exception) {
            return ActionResult.Error("Password generation failed: ${e.message}")
        }
    }
}