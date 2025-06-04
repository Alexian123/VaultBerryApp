package com.alexianhentiu.vaultberryapp.application.usecase.settings

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.common.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import kotlinx.coroutines.flow.Flow

class ObserveSettingUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider,
    private val settingsRepository: SettingsRepository
) : ObserveSettingUseCase {
    override operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<Flow<T>> {
        try {
            val flow = settingsRepository.observe(definition)
            return UseCaseResult.Success(flow)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.OBSERVE_SETTINGS_FAILURE,
                source = stringResourceProvider.getString(R.string.settings_repository_error_source),
                message = e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }
}