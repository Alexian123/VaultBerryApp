package com.alexianhentiu.vaultberryapp.application.usecase.settings

import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.ObserveSettingUseCase
import kotlinx.coroutines.flow.Flow

class ObserveSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveSettingUseCase {
    override operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<Flow<T>> {
        try {
            val flow = settingsRepository.observe(definition)
            return UseCaseResult.Success(flow)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.OBSERVE_SETTINGS_FAILURE,
                source = "Settings Repository",
                message = e.message ?: "Unknown error"
            )
        }
    }
}