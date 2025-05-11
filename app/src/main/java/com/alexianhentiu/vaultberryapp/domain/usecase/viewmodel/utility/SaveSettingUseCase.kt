package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingsManager
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingDefinition

class SaveSettingUseCase(private val settingsManager: SettingsManager) {
    suspend operator fun <T> invoke(definition: SettingDefinition<T>, value: T): UseCaseResult<Unit> {
        try {
            settingsManager.save(definition, value)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.EXTERNAL,
                source = "Settings Manager",
                message = "Error saving setting: ${e.message}"
            )
        }
    }
}