package com.alexianhentiu.vaultberryapp.di

import android.content.ClipboardManager
import com.alexianhentiu.vaultberryapp.di.qualifiers.DebugValidatorQualifier
import com.alexianhentiu.vaultberryapp.di.qualifiers.RegularValidatorQualifier
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RecoverySendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault.GetAllVaultEntryPreviewsUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault.GetDecryptedVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.Extract2FASecret
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.GetValidatorUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.LoadSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.ObserveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.SaveSettingUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.SettingsManager
import com.alexianhentiu.vaultberryapp.domain.utils.security.AuthGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelUseCaseModule {

    @Provides
    fun provideSaveSettingUseCase(
        settingsManager: SettingsManager
    ): SaveSettingUseCase = SaveSettingUseCase(settingsManager)

    @Provides
    fun provideLoadSettingUseCase(
        settingsManager: SettingsManager
    ): LoadSettingUseCase = LoadSettingUseCase(settingsManager)

    @Provides
    fun provideObserveSettingUseCase(
        settingsManager: SettingsManager
    ): ObserveSettingUseCase = ObserveSettingUseCase(settingsManager)

    @Provides
    fun provideEvalPasswordUseCase(
        passwordEvaluator: PasswordEvaluator
    ): EvalPasswordStrengthUseCase = EvalPasswordStrengthUseCase(passwordEvaluator)

    @Provides
    fun provideGetValidatorUseCase(
        @DebugValidatorQualifier debugValidator: InputValidator,
        @RegularValidatorQualifier regularValidator: InputValidator,
        loadSettingUseCase: LoadSettingUseCase
    ): GetValidatorUseCase = GetValidatorUseCase(
        debugValidator,
        regularValidator,
        loadSettingUseCase
    )

    @Provides
    fun provideCopyToClipboardUseCase(
        clipboardManager: ClipboardManager
    ): CopyToClipboardUseCase = CopyToClipboardUseCase(clipboardManager)

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
    fun provideGetAllVaultEntryPreviewsUseCase(
        vaultRepository: VaultRepository,
    ): GetAllVaultEntryPreviewsUseCase = GetAllVaultEntryPreviewsUseCase(vaultRepository)

    @Provides
    fun provideGetDecryptedVaultEntryUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase
    ): GetDecryptedVaultEntryUseCase = GetDecryptedVaultEntryUseCase(
        vaultRepository,
        decryptVaultEntryUseCase
    )

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