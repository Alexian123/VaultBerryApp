package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.EvalPasswordStrengthUseCase

class EvalPasswordStrengthUseCaseImpl(
    private val passwordEvaluator: PasswordEvaluator
) : EvalPasswordStrengthUseCase {

    override operator fun invoke(password: String): UseCaseResult<PasswordStrength> {
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