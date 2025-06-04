package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class EvalPasswordStrengthUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider,
    private val passwordEvaluator: PasswordEvaluator
) : EvalPasswordStrengthUseCase {

    override operator fun invoke(password: String): UseCaseResult<PasswordStrength> {
        try {
            val strength = passwordEvaluator.evaluateStrength(password)
            return UseCaseResult.Success(strength)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.PASSWORD_STRENGTH_EVALUATION_FAILURE,
                stringResourceProvider.getString(R.string.password_evaluator_error_source),
                e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }
}