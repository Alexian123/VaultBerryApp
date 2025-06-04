package com.alexianhentiu.vaultberryapp.data.di

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
    fun providePasswordGenerator(): PasswordGenerator = RandomPasswordGenerator()

    @Provides
    @Singleton
    fun providePasswordEvaluator(): PasswordEvaluator = DefaultPasswordEvaluator()

    @Provides
    @Singleton
    fun provideGeneralCryptoHandler(): GeneralCryptoHandler = AESHandler()

    @Provides
    @Singleton
    fun provideVaultSecurityHandler(
        cryptoHandler: GeneralCryptoHandler
    ): VaultSecurityHandler = VaultGuardian(cryptoHandler)

    @Provides
    @Singleton
    fun provideMessageExchangeAuthClient(): MessageExchangeAuthClient = ScramAuthClient()
}