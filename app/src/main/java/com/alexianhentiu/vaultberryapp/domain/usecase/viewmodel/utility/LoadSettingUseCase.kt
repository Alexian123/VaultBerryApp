package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingDefinition

class LoadSettingUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<T> {
        try {
            val value = settingsRepository.load(definition)
            return UseCaseResult.Success(value)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.LOAD_SETTINGS_FAILURE,
                source = "Application Settings",
                message = e.message ?: "Unknown error"
            )
        }
    }
}