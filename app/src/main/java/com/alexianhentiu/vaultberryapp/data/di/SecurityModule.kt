package com.alexianhentiu.vaultberryapp.data.di

import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidStringResourceProvider
import com.alexianhentiu.vaultberryapp.data.security.AESHandler
import com.alexianhentiu.vaultberryapp.data.security.ScramAuthClient
import com.alexianhentiu.vaultberryapp.data.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.data.security.password.DefaultPasswordEvaluator
import com.alexianhentiu.vaultberryapp.data.security.password.RandomPasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
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
    fun provideGeneralCryptoHandler(
        stringResourceProvider: AndroidStringResourceProvider,
    ): GeneralCryptoHandler = AESHandler(
        stringResourceProvider
    )

    @Provides
    @Singleton
    fun provideVaultSecurityHandler(
        base64Handler: Base64Handler,
        cryptoHandler: GeneralCryptoHandler
    ): VaultSecurityHandler = VaultGuardian(base64Handler, cryptoHandler)

    @Provides
    @Singleton
    fun provideMessageExchangeAuthClient(): MessageExchangeAuthClient = ScramAuthClient()
}