package com.alexianhentiu.vaultberryapp.domain.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength

interface EvalPasswordStrengthUseCase {
    operator fun invoke(password: String): UseCaseResult<PasswordStrength>
}