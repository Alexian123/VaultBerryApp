package com.alexianhentiu.vaultberryapp.domain.usecase.settings

import com.alexianhentiu.vaultberryapp.domain.model.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface LoadSettingUseCase {
    suspend operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<T>
}