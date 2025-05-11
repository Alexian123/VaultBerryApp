package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.SettingsManager
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.setting.AppSetting

class LoadSettingUseCase(private val settingsManager: SettingsManager) {
    suspend operator fun <T> invoke(definition: AppSetting<T>): UseCaseResult<T> {
        try {
            val value = settingsManager.load(definition)
            return UseCaseResult.Success(value)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.EXTERNAL,
                source = "Settings Manager",
                message = "Error loading setting: ${e.message}"
            )
        }
    }
}