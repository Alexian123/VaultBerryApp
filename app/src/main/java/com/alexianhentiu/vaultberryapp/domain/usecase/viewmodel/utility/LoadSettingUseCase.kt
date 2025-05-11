package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingsManager
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingDefinition

class LoadSettingUseCase(private val settingsManager: SettingsManager) {
    suspend operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<T> {
        try {
            val value = settingsManager.load(definition)
            return UseCaseResult.Success(value)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.EXTERNAL,
                source = "Settings Manager",
                message = "Error loading setting: ${e.message}"
            )
        }
    }
}