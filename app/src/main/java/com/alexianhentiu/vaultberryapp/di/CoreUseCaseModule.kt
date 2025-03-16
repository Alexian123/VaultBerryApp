package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeEmailUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeNameUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.GetRecoveryOTPUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.Verify2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CoreUseCaseModule {

    @Provides
    fun provideVerify2FAUseCase(
        userRepository: UserRepository,
        decryptKeyUseCase: DecryptKeyUseCase
    ): Verify2FAUseCase = Verify2FAUseCase(userRepository, decryptKeyUseCase)

    @Provides
    fun provideGetRecoveryOTPUseCase(
        userRepository: UserRepository
    ): GetRecoveryOTPUseCase = GetRecoveryOTPUseCase(userRepository)

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
        generateKeyChainUseCase: GenerateKeyChainUseCase
    ): ChangePasswordUseCase = ChangePasswordUseCase(
        accountRepository,
        generatePasswordUseCase,
        generateKeyChainUseCase
    )

    @Provides
    fun provideGetAccountUseCase(
        accountRepository: AccountRepository
    ): GetAccountUseCase = GetAccountUseCase(accountRepository)

    @Provides
    fun provideDeleteAccountUseCase(
        accountRepository: AccountRepository
    ): DeleteAccountUseCase = DeleteAccountUseCase(accountRepository)

    @Provides
    fun provideSetup2FAUseCase(
        accountRepository: AccountRepository,
        extract2FASecret: Extract2FASecret
    ): Setup2FAUseCase = Setup2FAUseCase(accountRepository, extract2FASecret)

    @Provides
    fun provideDisable2FAUseCase(
        accountRepository: AccountRepository
    ): Disable2FAUseCase = Disable2FAUseCase(accountRepository)

    @Provides
    fun provideGet2FAStatusUseCase(
        accountRepository: AccountRepository
    ): Get2FAStatusUseCase = Get2FAStatusUseCase(accountRepository)
}