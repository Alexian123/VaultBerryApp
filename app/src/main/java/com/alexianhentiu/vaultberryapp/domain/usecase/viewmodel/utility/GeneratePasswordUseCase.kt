package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordGenerator

class GeneratePasswordUseCase(
    private val passwordGenerator: PasswordGenerator
) {
    operator fun invoke(
        length: Int,
        includeUppercase: Boolean,
        includeNumbers: Boolean,
        includeSpecialChars: Boolean,
        includeSpaces: Boolean
    ): UseCaseResult<String> {
        try {
            return UseCaseResult.Success(
                passwordGenerator.generatePassword(
                    length = length,
                    includeUppercase = includeUppercase,
                    includeNumbers = includeNumbers,
                    includeSpecialChars = includeSpecialChars,
                    includeSpaces = includeSpaces
                )
            )
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Password Generator",
                "Password generation failed: ${e.message}"
            )
        }
    }
}