package com.alexianhentiu.vaultberryapp.di

import android.hardware.SensorManager
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeEmailUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeNameUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.sensor.RegisterListenerUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.sensor.UnregisterListenerUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.vault.ReEncryptAllEntriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CoreUseCaseModule {
    @Provides
    fun provideRecoveryLoginUseCase(
        userRepository: UserRepository,
        decryptKeyUseCase: DecryptKeyUseCase
    ): RecoveryLoginUseCase = RecoveryLoginUseCase(userRepository, decryptKeyUseCase)

    @Provides
    fun provideRegisterUseCase(
        userRepository: UserRepository,
        generatePasswordUseCase: GeneratePasswordUseCase,
        generateKeyChainUseCase: GenerateKeyChainUseCase
    ): RegisterUseCase = RegisterUseCase(
        userRepository,
        generatePasswordUseCase,
        generateKeyChainUseCase
    )

    @Provides
    fun provideLoginUseCase(
        userRepository: UserRepository,
        decryptKeyUseCase: DecryptKeyUseCase
    ): LoginUseCase = LoginUseCase(userRepository, decryptKeyUseCase)

    @Provides
    fun provideLogoutUseCase(
        userRepository: UserRepository
    ): LogoutUseCase = LogoutUseCase(userRepository)

    @Provides
    fun provideGetEntriesUseCase(
        vaultEntryRepository: VaultEntryRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase
    ): GetEntriesUseCase = GetEntriesUseCase(vaultEntryRepository, decryptVaultEntryUseCase)

    @Provides
    fun provideAddEntryUseCase(
        vaultEntryRepository: VaultEntryRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): AddEntryUseCase = AddEntryUseCase(vaultEntryRepository, encryptVaultEntryUseCase)

    @Provides
    fun provideUpdateEntryUseCase(
        vaultEntryRepository: VaultEntryRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): UpdateEntryUseCase = UpdateEntryUseCase(vaultEntryRepository, encryptVaultEntryUseCase)

    @Provides
    fun provideDeleteEntryUseCase(
        vaultEntryRepository: VaultEntryRepository
    ): DeleteEntryUseCase = DeleteEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideRegisterListenerUseCase(
        sensorManager: SensorManager
    ): RegisterListenerUseCase = RegisterListenerUseCase(sensorManager)

    @Provides
    fun provideUnregisterListenerUseCase(
        sensorManager: SensorManager
    ): UnregisterListenerUseCase = UnregisterListenerUseCase(sensorManager)

    @Provides
    fun provideChangeEmailUseCase(
        accountRepository: AccountRepository
    ): ChangeEmailUseCase = ChangeEmailUseCase(accountRepository)

    @Provides
    fun provideChangeNameUseCase(
        accountRepository: AccountRepository
    ): ChangeNameUseCase = ChangeNameUseCase(accountRepository)

    @Provides
    fun provideChangePasswordUseCase(
        accountRepository: AccountRepository,
        generatePasswordUseCase: GeneratePasswordUseCase,
        decryptKeyUseCase: DecryptKeyUseCase,
        generateKeyChainUseCase: GenerateKeyChainUseCase,
        reEncryptAllEntriesUseCase: ReEncryptAllEntriesUseCase
    ): ChangePasswordUseCase = ChangePasswordUseCase(
        accountRepository,
        generatePasswordUseCase,
        decryptKeyUseCase,
        generateKeyChainUseCase,
        reEncryptAllEntriesUseCase
    )

    @Provides
    fun provideGetAccountUseCase(
        accountRepository: AccountRepository
    ): GetAccountUseCase = GetAccountUseCase(accountRepository)

    @Provides
    fun provideDeleteAccountUseCase(
        accountRepository: AccountRepository
    ): DeleteAccountUseCase = DeleteAccountUseCase(accountRepository)
}