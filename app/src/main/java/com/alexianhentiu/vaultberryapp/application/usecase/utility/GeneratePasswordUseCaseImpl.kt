package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class GeneratePasswordUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider,
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
                stringResourceProvider.getString(R.string.password_generator_error_source),
                e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }
}