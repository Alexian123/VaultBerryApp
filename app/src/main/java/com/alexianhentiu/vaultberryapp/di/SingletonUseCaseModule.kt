package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.auth.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.auth.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonUseCaseModule {

    @Provides
    @Singleton
    fun provideExtract2FASecret(): Extract2FASecret = Extract2FASecret()

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
    fun provideGeneratePasswordPairUseCase(
        passwordGenerator: PasswordGenerator
    ): GeneratePasswordPairUseCase = GeneratePasswordPairUseCase(passwordGenerator)

    @Provides
    @Singleton
    fun provideReEncryptVaultUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): ReEncryptVaultUseCase = ReEncryptVaultUseCase(
        vaultRepository,
        decryptVaultEntryUseCase,
        encryptVaultEntryUseCase
    )
}