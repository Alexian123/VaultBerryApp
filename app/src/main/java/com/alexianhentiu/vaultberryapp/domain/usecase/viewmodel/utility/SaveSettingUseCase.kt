package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingDefinition

class SaveSettingUseCase(private val settingsRepository: SettingsRepository) {

    suspend operator fun <T> invoke(
        definition: SettingDefinition<T>,
        value: T
    ): UseCaseResult<Unit> {
        try {
            settingsRepository.save(definition, value)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.EXTERNAL,
                source = "Settings Repository",
                message = "Error saving setting: ${e.message}"
            )
        }
    }
}