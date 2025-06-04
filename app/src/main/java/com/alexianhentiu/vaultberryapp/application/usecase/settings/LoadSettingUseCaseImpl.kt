package com.alexianhentiu.vaultberryapp.application.usecase.settings

import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.LoadSettingUseCase

class LoadSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : LoadSettingUseCase {
    override suspend operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<T> {
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