package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.types.ValidatedFieldType
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator

class GetValidatorUseCase(private val inputValidator: InputValidator) {

    operator fun invoke(
        type: ValidatedFieldType
    ): UseCaseResult<(String) -> Boolean> {
        try {
            return when (type) {
                ValidatedFieldType.EMAIL -> {
                    UseCaseResult.Success(inputValidator::validateEmail)
                }
                ValidatedFieldType.PASSWORD -> {
                    UseCaseResult.Success(inputValidator::validatePassword)
                }
                ValidatedFieldType.OTP -> {
                    UseCaseResult.Success(inputValidator::validateOTP)
                }
                ValidatedFieldType.MFA_CODE -> {
                    UseCaseResult.Success(inputValidator::validate2FACode)
                }
                ValidatedFieldType.ENTRY_TITLE -> {
                    UseCaseResult.Success(inputValidator::validateEntryTitle)
                }
                ValidatedFieldType.NONE -> {
                    UseCaseResult.Success({ true })
                }
            }
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.INTERNAL,
                source = "Validation",
                message = "Validation failed: ${e.message}"
            )
        }
    }
}