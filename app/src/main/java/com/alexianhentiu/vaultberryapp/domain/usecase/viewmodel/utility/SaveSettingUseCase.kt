package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.SettingsManager
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.setting.AppSetting

class SaveSettingUseCase(private val settingsManager: SettingsManager) {
    suspend operator fun <T> invoke(definition: AppSetting<T>, value: T): UseCaseResult<Unit> {
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