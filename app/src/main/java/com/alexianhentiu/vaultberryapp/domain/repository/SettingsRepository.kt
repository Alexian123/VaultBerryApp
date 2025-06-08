package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.SettingDefinition
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun <T> load(setting: SettingDefinition<T>): T

    suspend fun <T> save(setting: SettingDefinition<T>, value: T)

    fun <T> observe(setting: SettingDefinition<T>): Flow<T>

}