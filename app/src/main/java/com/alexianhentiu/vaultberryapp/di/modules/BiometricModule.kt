package com.alexianhentiu.vaultberryapp.di.modules

import android.content.Context
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricAuthManager
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricCryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BiometricModule {

    @Provides
    @Singleton
    fun provideBiometricAuthManager(
        cryptoManager: BiometricCryptoManager,
        credentialsRepository: CredentialsRepository,
        @ApplicationContext context: Context
    ): BiometricAuthManager = BiometricAuthManager(cryptoManager, credentialsRepository, context)

    @Provides
    @Singleton
    fun provideBiometricCryptoManager(): BiometricCryptoManager = BiometricCryptoManager()
}