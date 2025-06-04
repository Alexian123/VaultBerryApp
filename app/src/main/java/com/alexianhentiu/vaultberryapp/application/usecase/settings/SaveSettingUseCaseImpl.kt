package com.alexianhentiu.vaultberryapp.application.usecase.settings

import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.common.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.SaveSettingUseCase

class SaveSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SaveSettingUseCase {

    override suspend operator fun <T> invoke(
        definition: SettingDefinition<T>,
        value: T
    ): UseCaseResult<Unit> {
        try {
            settingsRepository.save(definition, value)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.SAVE_SETTINGS_FAILURE,
                source = "Settings Repository",
                message = e.message ?: "Unknown error"
            )
        }
    }
}