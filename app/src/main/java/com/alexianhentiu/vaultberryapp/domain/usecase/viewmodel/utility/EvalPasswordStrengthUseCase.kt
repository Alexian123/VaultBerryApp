package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength

class EvalPasswordStrengthUseCase(
    private val passwordEvaluator: PasswordEvaluator
) {

    operator fun invoke(password: String): UseCaseResult<PasswordStrength> {
        try {
            val strength = passwordEvaluator.evaluateStrength(password)
            return UseCaseResult.Success(strength)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.PASSWORD_STRENGTH_EVALUATION_FAILURE,
                "Password Evaluator",
                e.message ?: "Unknown error"
            )
        }
    }
}