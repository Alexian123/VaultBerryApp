package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.ReEncryptAllEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpecificUseCaseModule {

    @Provides
    @Singleton
    fun provideExtract2FASecret(): Extract2FASecret = Extract2FASecret()

    @Provides
    @Singleton
    fun provideReEncryptAllEntriesUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): ReEncryptAllEntriesUseCase = ReEncryptAllEntriesUseCase(
        vaultRepository,
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
    fun provideGeneratePasswordPairUseCase(
        passwordGenerator: PasswordGenerator
    ): GeneratePasswordPairUseCase = GeneratePasswordPairUseCase(passwordGenerator)
}