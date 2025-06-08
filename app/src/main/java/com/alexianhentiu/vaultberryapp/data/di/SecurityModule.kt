package com.alexianhentiu.vaultberryapp.data.di

import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidStringResourceProvider
import com.alexianhentiu.vaultberryapp.data.security.AESHandler
import com.alexianhentiu.vaultberryapp.data.security.PBKDF2SHA256Handler
import com.alexianhentiu.vaultberryapp.data.security.ScramAuthClient
import com.alexianhentiu.vaultberryapp.data.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.data.security.password.DefaultPasswordEvaluator
import com.alexianhentiu.vaultberryapp.data.security.password.RandomPasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import com.alexianhentiu.vaultberryapp.domain.security.KeyDerivationHandler
import com.alexianhentiu.vaultberryapp.domain.security.MessageExchangeAuthClient
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun providePasswordGenerator(
        stringResourceProvider: AndroidStringResourceProvider,
    ): PasswordGenerator = RandomPasswordGenerator(
        stringResourceProvider
    )

    @Provides
    @Singleton
    fun providePasswordEvaluator(): PasswordEvaluator = DefaultPasswordEvaluator()

    @Provides
    @Singleton
    fun provideGeneralCryptoHandler(): GeneralCryptoHandler = AESHandler()

    @Provides
    @Singleton
    fun provideKeyDerivationHandler(
        stringResourceProvider: AndroidStringResourceProvider,
    ): KeyDerivationHandler = PBKDF2SHA256Handler(
        stringResourceProvider
    )

    @Provides
    @Singleton
    fun provideVaultSecurityHandler(
        base64Handler: Base64Handler,
        cryptoHandler: GeneralCryptoHandler,
        keyDerivationHandler: KeyDerivationHandler
    ): VaultSecurityHandler = VaultGuardian(base64Handler, cryptoHandler, keyDerivationHandler)

    @Provides
    @Singleton
    fun provideMessageExchangeAuthClient(): MessageExchangeAuthClient = ScramAuthClient()
}