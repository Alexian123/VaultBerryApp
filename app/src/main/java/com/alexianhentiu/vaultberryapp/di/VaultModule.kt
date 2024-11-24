package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.AESHandler
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.CryptographyHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VaultModule {

    @Provides
    @Singleton
    fun provideCryptoHandler(): CryptographyHandler = AESHandler("CBC/PKCS5Padding")

    @Provides
    @Singleton
    fun provideVaultGuardian(cryptoHandler: CryptographyHandler): VaultGuardian = VaultGuardian(cryptoHandler)
}