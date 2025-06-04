package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GeneratePasswordUseCase

class GeneratePasswordUseCaseImpl(
    private val passwordGenerator: PasswordGenerator
) : GeneratePasswordUseCase {
    override operator fun invoke(options: PasswordGenOptions): UseCaseResult<String> {
        return try {
            UseCaseResult.Success(
                passwordGenerator.generate(options)
            )
        } catch (e: Exception) {
            UseCaseResult.Error(
                ErrorType.PASSWORD_GENERATION_FAILURE,
                "Password Generator",
                e.message ?: "Unknown error"
            )
        }
    }
}