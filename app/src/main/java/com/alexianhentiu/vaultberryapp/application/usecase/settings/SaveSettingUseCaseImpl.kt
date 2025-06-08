package com.alexianhentiu.vaultberryapp.application.usecase.settings

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.model.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.SaveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class SaveSettingUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider,
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
                ErrorInfo(
                    type = ErrorType.SAVE_SETTINGS_FAILURE,
                    source = stringResourceProvider.getString(
                        R.string.settings_repository_error_source
                    ),
                    message = e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}