package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.di.qualifiers.DebugValidatorQualifier
import com.alexianhentiu.vaultberryapp.di.qualifiers.RegularValidatorQualifier
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.types.setting.BooleanSetting
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator

class GetValidatorUseCase(
    @DebugValidatorQualifier private val debugValidator: InputValidator,
    @RegularValidatorQualifier private val regularValidator: InputValidator,
    private val loadSettingUseCase: LoadSettingUseCase
) {

    suspend operator fun invoke(): UseCaseResult<InputValidator> {
        try {
            val result = loadSettingUseCase(BooleanSetting("debug_mode", false))
            if (result is UseCaseResult.Error) return result
            val debugMode = (result as UseCaseResult.Success).data
            val inputValidator = if (debugMode) debugValidator else regularValidator
            return UseCaseResult.Success(inputValidator)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.INTERNAL,
                source = "Validation",
                message = "Validation failed: ${e.message}"
            )
        }
    }
}