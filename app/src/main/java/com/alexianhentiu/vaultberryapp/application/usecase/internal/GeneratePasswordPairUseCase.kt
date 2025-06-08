package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import javax.inject.Inject

class GeneratePasswordPairUseCase @Inject constructor(
    private val stringResourceProvider: StringResourceProvider,
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
                ErrorInfo(
                    ErrorType.PASSWORD_PAIR_GENERATION_FAILURE,
                    stringResourceProvider.getString(R.string.password_generator_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}