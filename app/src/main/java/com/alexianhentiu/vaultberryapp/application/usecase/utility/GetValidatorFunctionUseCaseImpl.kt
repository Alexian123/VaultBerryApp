package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetValidatorFunctionUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator

class GetValidatorFunctionUseCaseImpl(
    private val laxValidator: InputValidator,
    private val strictValidator: InputValidator,
    private val stringResourceProvider: StringResourceProvider
) : GetValidatorFunctionUseCase {
    override operator fun invoke(
        type: InputType,
        lax: Boolean
    ): UseCaseResult<(String) -> Boolean> {
        try {
            if (lax) return UseCaseResult.Success(laxValidator.getValidatorFunction(type))
            return UseCaseResult.Success(strictValidator.getValidatorFunction(type))
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}