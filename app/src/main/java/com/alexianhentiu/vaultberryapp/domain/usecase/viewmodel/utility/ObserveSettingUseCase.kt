package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility

import com.alexianhentiu.vaultberryapp.domain.utils.SettingsManager
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.setting.AppSetting
import kotlinx.coroutines.flow.Flow

class ObserveSettingUseCase(private val settingsManager: SettingsManager) {
    operator fun <T> invoke(definition: AppSetting<T>): UseCaseResult<Flow<T>> {
        try {
            val flow = settingsManager.observe(definition)
            return UseCaseResult.Success(flow)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                type = ErrorType.EXTERNAL,
                source = "Settings Manager",
                message = "Error observing setting: ${e.message}"
            )
        }
    }
}