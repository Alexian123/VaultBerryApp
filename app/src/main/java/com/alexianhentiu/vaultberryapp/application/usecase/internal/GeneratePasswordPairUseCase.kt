package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.domain.common.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import javax.inject.Inject

class GeneratePasswordPairUseCase @Inject constructor(
    private val passwordGenerator: PasswordGenerator,
) {

    operator fun invoke(password: String): UseCaseResult<PasswordPair> {
        try {
            val recoveryPassword = passwordGenerator.generate(
                PasswordGenOptions.builder()
                    .length(20)
                    .includeUppercase(true)
                    .includeNumbers(true)
                    .includeSpecialChars(true)
                    .includeSpaces(false)
                    .build()
            )
            return UseCaseResult.Success(
                PasswordPair(
                    regularPassword = password,
                    recoveryPassword = recoveryPassword,
                )
            )
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.PASSWORD_PAIR_GENERATION_FAILURE,
                "Auth Guardian",
                e.message ?: "Unknown error"
            )
        }
    }
}