package com.alexianhentiu.vaultberryapp.di

import android.hardware.SensorManager
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.UpdateAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.UpdateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.GetRecoveryKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoveryLoginUseCase
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
    fun provideGetRecoveryKeyUseCase(userRepository: UserRepository): GetRecoveryKeyUseCase =
        GetRecoveryKeyUseCase(userRepository)

    @Provides
    fun provideRecoveryLoginUseCase(userRepository: UserRepository): RecoveryLoginUseCase =
        RecoveryLoginUseCase(userRepository)

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
    fun provideGenerateKeyChainUseCase(vaultGuardian: VaultGuardian): GenerateKeyChainUseCase =
        GenerateKeyChainUseCase(vaultGuardian)

    @Provides
    fun provideDecryptKeyUseCase(vaultGuardian: VaultGuardian): DecryptKeyUseCase =
        DecryptKeyUseCase(vaultGuardian)

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

    @Provides
    fun provideUpdateKeyChainUseCase(accountRepository: AccountRepository): UpdateKeyChainUseCase =
        UpdateKeyChainUseCase(accountRepository)

    @Provides
    fun provideGetAccountUseCase(accountRepository: AccountRepository): GetAccountUseCase =
        GetAccountUseCase(accountRepository)

    @Provides
    fun provideUpdateAccountUseCase(accountRepository: AccountRepository): UpdateAccountUseCase =
        UpdateAccountUseCase(accountRepository)

    @Provides
    fun provideDeleteAccountUseCase(accountRepository: AccountRepository): DeleteAccountUseCase =
        DeleteAccountUseCase(accountRepository)
}