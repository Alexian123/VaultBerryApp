package com.alexianhentiu.vaultberryapp.application.di

import com.alexianhentiu.vaultberryapp.application.usecase.auth.ActivationSendUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.auth.LoginUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.auth.LogoutUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.auth.RecoveryLoginUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.auth.RecoverySendUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.auth.RegisterUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.security.MessageExchangeAuthClient
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.ActivationSendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoverySendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AuthUseCaseModule {

    @Provides
    fun provideActivationSendUseCase(
        authRepository: AuthRepository,
        stringResourceProvider: StringResourceProvider
    ): ActivationSendUseCase = ActivationSendUseCaseImpl(
        authRepository,
        stringResourceProvider
    )

    @Provides
    fun provideLoginUseCase(
        stringResourceProvider: StringResourceProvider,
        authRepository: AuthRepository,
        messageExchangeAuthClient: MessageExchangeAuthClient,
        decryptKeyUseCase: DecryptKeyUseCase
    ): LoginUseCase = LoginUseCaseImpl(
        stringResourceProvider,
        authRepository,
        messageExchangeAuthClient,
        decryptKeyUseCase
    )

    @Provides
    fun provideLogoutUseCase(
        authRepository: AuthRepository,
        stringResourceProvider: StringResourceProvider
    ): LogoutUseCase = LogoutUseCaseImpl(
        authRepository,
        stringResourceProvider
    )

    @Provides
    fun provideRecoveryLoginUseCase(
        authRepository: AuthRepository,
        decryptKeyUseCase: DecryptKeyUseCase,
        stringResourceProvider: StringResourceProvider
    ): RecoveryLoginUseCase = RecoveryLoginUseCaseImpl(
        authRepository,
        decryptKeyUseCase,
        stringResourceProvider
    )

    @Provides
    fun provideRecoverySendUseCase(
        authRepository: AuthRepository,
        stringResourceProvider: StringResourceProvider
    ): RecoverySendUseCase = RecoverySendUseCaseImpl(
        authRepository,
        stringResourceProvider
    )

    @Provides
    fun provideRegisterUseCase(
        authRepository: AuthRepository,
        generatePasswordPairUseCase: GeneratePasswordPairUseCase,
        generateKeyChainUseCase: GenerateKeyChainUseCase,
        stringResourceProvider: StringResourceProvider
    ): RegisterUseCase = RegisterUseCaseImpl(
        authRepository,
        generatePasswordPairUseCase,
        generateKeyChainUseCase,
        stringResourceProvider
    )
}