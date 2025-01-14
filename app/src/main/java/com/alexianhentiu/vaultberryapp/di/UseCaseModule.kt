package com.alexianhentiu.vaultberryapp.di

import android.hardware.SensorManager
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.EncryptVaultKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptVaultKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.sensor.RegisterListenerUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.sensor.UnregisterListenerUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideRegisterUseCase(userRepository: UserRepository): RegisterUseCase =
        RegisterUseCase(userRepository)

    @Provides
    fun provideLoginUseCase(userRepository: UserRepository): LoginUseCase =
        LoginUseCase(userRepository)

    @Provides
    fun provideLogoutUseCase(userRepository: UserRepository): LogoutUseCase =
        LogoutUseCase(userRepository)

    @Provides
    fun provideGetEntriesUseCase(vaultEntryRepository: VaultEntryRepository): GetEntriesUseCase =
        GetEntriesUseCase(vaultEntryRepository)

    @Provides
    fun provideAddEntryUseCase(vaultEntryRepository: VaultEntryRepository): AddEntryUseCase =
        AddEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideUpdateEntryUseCase(vaultEntryRepository: VaultEntryRepository): UpdateEntryUseCase =
        UpdateEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideDeleteEntryUseCase(vaultEntryRepository: VaultEntryRepository): DeleteEntryUseCase =
        DeleteEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideEncryptVaultKeyUseCase(vaultGuardian: VaultGuardian): EncryptVaultKeyUseCase =
        EncryptVaultKeyUseCase(vaultGuardian)

    @Provides
    fun provideDecryptVaultKeyUseCase(vaultGuardian: VaultGuardian): DecryptVaultKeyUseCase =
        DecryptVaultKeyUseCase(vaultGuardian)

    @Provides
    fun provideDecryptVaultEntryUseCase(vaultGuardian: VaultGuardian): DecryptVaultEntryUseCase =
        DecryptVaultEntryUseCase(vaultGuardian)

    @Provides
    fun provideEncryptVaultEntryUseCase(vaultGuardian: VaultGuardian): EncryptVaultEntryUseCase =
        EncryptVaultEntryUseCase(vaultGuardian)

    @Provides
    fun provideRegisterListenerUseCase(sensorManager: SensorManager): RegisterListenerUseCase =
        RegisterListenerUseCase(sensorManager)

    @Provides
    fun provideUnregisterListenerUseCase(sensorManager: SensorManager): UnregisterListenerUseCase =
        UnregisterListenerUseCase(sensorManager)
}