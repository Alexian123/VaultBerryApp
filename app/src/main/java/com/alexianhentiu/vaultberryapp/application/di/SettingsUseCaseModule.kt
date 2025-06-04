package com.alexianhentiu.vaultberryapp.application.di

import com.alexianhentiu.vaultberryapp.application.usecase.settings.LoadSettingUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.settings.ObserveSettingUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.settings.SaveSettingUseCaseImpl
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.LoadSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.settings.SaveSettingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SettingsUseCaseModule {

    @Provides
    fun provideLoadSettingUseCase(
        settingsRepository: SettingsRepository
    ): LoadSettingUseCase = LoadSettingUseCaseImpl(settingsRepository)

    @Provides
    fun provideObserveSettingUseCase(
        settingsRepository: SettingsRepository
    ): ObserveSettingUseCase = ObserveSettingUseCaseImpl(settingsRepository)

    @Provides
    fun provideSaveSettingUseCase(
        settingsRepository: SettingsRepository
    ): SaveSettingUseCase = SaveSettingUseCaseImpl(settingsRepository)
}