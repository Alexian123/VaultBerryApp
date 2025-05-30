package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.settings.SettingDefinition
import kotlinx.coroutines.flow.Flow

class ObserveSettingUseCase(private val settingsRepository: SettingsRepository) {
    operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<Flow<T>> {
        try {
            val flow = settingsRepository.observe(definition)
            return UseCaseResult.Success(flow)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.EXTERNAL,
                source = "Settings Repository",
                message = "Error observing setting: ${e.message}"
            )
        }
    }
}