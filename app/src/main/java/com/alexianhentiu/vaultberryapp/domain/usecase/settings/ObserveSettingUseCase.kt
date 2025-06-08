package com.alexianhentiu.vaultberryapp.domain.usecase.settings

import com.alexianhentiu.vaultberryapp.domain.model.SettingDefinition
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface ObserveSettingUseCase {
    operator fun <T> invoke(definition: SettingDefinition<T>): UseCaseResult<Flow<T>>
}