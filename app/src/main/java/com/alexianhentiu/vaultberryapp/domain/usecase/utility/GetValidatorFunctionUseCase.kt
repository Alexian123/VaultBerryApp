package com.alexianhentiu.vaultberryapp.domain.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType

interface GetValidatorFunctionUseCase {
    operator fun invoke(
        type: InputType,
        lax: Boolean = false
    ): UseCaseResult<(String) -> Boolean>
}