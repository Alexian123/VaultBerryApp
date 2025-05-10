package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.PasswordStrength

class EvalPasswordStrengthUseCase(
    private val passwordEvaluator: PasswordEvaluator
) {

    operator fun invoke(password: String): UseCaseResult<PasswordStrength> {
        try {
            val strength = passwordEvaluator.evaluateStrength(password)
            return UseCaseResult.Success(strength)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Password Evaluator",
                "Password strength evaluation failed: ${e.message}"
            )
        }
    }
}