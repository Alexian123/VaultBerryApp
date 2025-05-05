package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.RecoverySendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.security.AuthGuardian
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object GeneralUseCaseModule {

    @Provides
    fun provideRecoverySendUseCase(
        authRepository: AuthRepository
    ): RecoverySendUseCase = RecoverySendUseCase(authRepository)

    @Provides
    fun provideRecoveryLoginUseCase(
        authRepository: AuthRepository,
        decryptKeyUseCase: DecryptKeyUseCase
    ): RecoveryLoginUseCase = RecoveryLoginUseCase(authRepository, decryptKeyUseCase)

    @Provides
    fun provideRegisterUseCase(
        authRepository: AuthRepository,
        generatePasswordPairUseCase: GeneratePasswordPairUseCase,
        generateKeyChainUseCase: GenerateKeyChainUseCase
    ): RegisterUseCase = RegisterUseCase(
        authRepository,
        generatePasswordPairUseCase,
        generateKeyChainUseCase
    )

    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        authGuardian: AuthGuardian,
        decryptKeyUseCase: DecryptKeyUseCase
    ): LoginUseCase = LoginUseCase(
        authRepository,
        authGuardian,
        decryptKeyUseCase
    )

    @Provides
    fun provideLogoutUseCase(
        authRepository: AuthRepository
    ): LogoutUseCase = LogoutUseCase(authRepository)

    @Provides
    fun provideGetEntriesUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase
    ): GetEntriesUseCase = GetEntriesUseCase(vaultRepository, decryptVaultEntryUseCase)

    @Provides
    fun provideAddEntryUseCase(
        vaultRepository: VaultRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): AddEntryUseCase = AddEntryUseCase(vaultRepository, encryptVaultEntryUseCase)

    @Provides
    fun provideUpdateEntryUseCase(
        vaultRepository: VaultRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): UpdateEntryUseCase = UpdateEntryUseCase(vaultRepository, encryptVaultEntryUseCase)

    @Provides
    fun provideDeleteEntryUseCase(
        vaultRepository: VaultRepository
    ): DeleteEntryUseCase = DeleteEntryUseCase(vaultRepository)

    @Provides
    fun provideChangeAccountInfoUseCase(
        accountRepository: AccountRepository
    ): ChangeAccountInfoUseCase = ChangeAccountInfoUseCase(accountRepository)

    @Provides
    fun provideChangePasswordUseCase(
        accountRepository: AccountRepository,
        generatePasswordPairUseCase: GeneratePasswordPairUseCase,
        generateKeyChainUseCase: GenerateKeyChainUseCase,
        decryptKeyUseCase: DecryptKeyUseCase,
        reEncryptVaultUseCase: ReEncryptVaultUseCase
    ): ChangePasswordUseCase = ChangePasswordUseCase(
        accountRepository,
        generatePasswordPairUseCase,
        generateKeyChainUseCase,
        decryptKeyUseCase,
        reEncryptVaultUseCase
    )

    @Provides
    fun provideGetAccountInfoUseCase(
        accountRepository: AccountRepository
    ): GetAccountInfoUseCase = GetAccountInfoUseCase(accountRepository)

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