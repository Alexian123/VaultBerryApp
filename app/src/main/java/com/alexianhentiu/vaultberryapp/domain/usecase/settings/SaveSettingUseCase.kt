package com.alexianhentiu.vaultberryapp.domain.usecase.settings

import com.alexianhentiu.vaultberryapp.domain.common.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface SaveSettingUseCase {
    suspend operator fun <T> invoke(
        definition: SettingDefinition<T>,
        value: T
    ): UseCaseResult<Unit>
}