package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetValidatorFunctionUseCase
import com.alexianhentiu.vaultberryapp.domain.validation.InputValidator

class GetValidatorFunctionUseCaseImpl(
    private val laxValidator: InputValidator,
    private val strictValidator: InputValidator,
) : GetValidatorFunctionUseCase {
    override operator fun invoke(
        type: InputType,
        lax: Boolean
    ): UseCaseResult<(String) -> Boolean> {
        if (lax) return UseCaseResult.Success(laxValidator.getValidatorFunction(type))
        return UseCaseResult.Success(strictValidator.getValidatorFunction(type))
    }
}