package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.vault.ReEncryptAllEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExtraUseCaseModule {

    @Provides
    fun provideExtract2FASecret(): Extract2FASecret = Extract2FASecret()

    @Provides
    fun provideReEncryptAllEntriesUseCase(
        vaultEntryRepository: VaultEntryRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): ReEncryptAllEntriesUseCase = ReEncryptAllEntriesUseCase(
        vaultEntryRepository,
        decryptVaultEntryUseCase,
        encryptVaultEntryUseCase
    )

    @Provides
    @Singleton
    fun provideGenerateKeyChainUseCase(
        vaultGuardian: VaultGuardian
    ): GenerateKeyChainUseCase = GenerateKeyChainUseCase(vaultGuardian)

    @Provides
    @Singleton
    fun provideDecryptKeyUseCase(
        vaultGuardian: VaultGuardian
    ): DecryptKeyUseCase = DecryptKeyUseCase(vaultGuardian)

    @Provides
    @Singleton
    fun provideDecryptVaultEntryUseCase(
        vaultGuardian: VaultGuardian
    ): DecryptVaultEntryUseCase = DecryptVaultEntryUseCase(vaultGuardian)

    @Provides
    @Singleton
    fun provideEncryptVaultEntryUseCase(
        vaultGuardian: VaultGuardian
    ): EncryptVaultEntryUseCase = EncryptVaultEntryUseCase(vaultGuardian)

    @Provides
    @Singleton
    fun provideGeneratePasswordUseCase(
        passwordGenerator: PasswordGenerator
    ): GeneratePasswordUseCase = GeneratePasswordUseCase(passwordGenerator)
}