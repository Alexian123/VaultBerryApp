package com.alexianhentiu.vaultberryapp.application.di

import com.alexianhentiu.vaultberryapp.application.usecase.account.ChangeAccountInfoUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.account.ChangePasswordUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.account.DeleteAccountUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.account.Disable2FAUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.account.Get2FAStatusUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.account.GetAccountInfoUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.account.Setup2FAUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.Extract2FASecretUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Setup2FAUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AccountUseCaseModule {

    @Provides
    fun provideChangeAccountInfoUseCase(
        accountRepository: AccountRepository
    ): ChangeAccountInfoUseCase = ChangeAccountInfoUseCaseImpl(accountRepository)

    @Provides
    fun provideChangePasswordUseCase(
        accountRepository: AccountRepository,
        generatePasswordPairUseCase: GeneratePasswordPairUseCase,
        generateKeyChainUseCase: GenerateKeyChainUseCase,
        decryptKeyUseCase: DecryptKeyUseCase,
        reEncryptVaultUseCase: ReEncryptVaultUseCase
    ): ChangePasswordUseCase = ChangePasswordUseCaseImpl(
        accountRepository,
        generatePasswordPairUseCase,
        generateKeyChainUseCase,
        decryptKeyUseCase,
        reEncryptVaultUseCase
    )

    @Provides
    fun provideDeleteAccountUseCase(
        accountRepository: AccountRepository
    ): DeleteAccountUseCase = DeleteAccountUseCaseImpl(accountRepository)

    @Provides
    fun provideDisable2FAUseCase(
        accountRepository: AccountRepository
    ): Disable2FAUseCase = Disable2FAUseCaseImpl(accountRepository)

    @Provides
    fun provideGet2FAStatusUseCase(
        accountRepository: AccountRepository
    ): Get2FAStatusUseCase = Get2FAStatusUseCaseImpl(accountRepository)

    @Provides
    fun provideGetAccountInfoUseCase(
        accountRepository: AccountRepository
    ): GetAccountInfoUseCase = GetAccountInfoUseCaseImpl(accountRepository)

    @Provides
    fun provideSetup2FAUseCase(
        accountRepository: AccountRepository,
        extract2FASecretUseCase: Extract2FASecretUseCase
    ): Setup2FAUseCase = Setup2FAUseCaseImpl(accountRepository, extract2FASecretUseCase)
}