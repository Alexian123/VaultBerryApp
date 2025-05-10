package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import com.alexianhentiu.vaultberryapp.domain.model.entity.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GeneratePasswordPairUseCase(
    private val passwordGenerator: PasswordGenerator
) {

    operator fun invoke(password: String): UseCaseResult<PasswordPair> {
        try {
            val recoveryPassword = passwordGenerator.generatePassword(
                length = 20,
                includeUppercase = true,
                includeNumbers = true,
                includeSpecialChars = true,
                includeSpaces = false
            )
            return UseCaseResult.Success(
                PasswordPair(
                    regularPassword = password,
                    recoveryPassword = recoveryPassword,
                )
            )
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Auth Guardian",
                "Password pair generation failed: ${e.message}"
            )
        }
    }
}