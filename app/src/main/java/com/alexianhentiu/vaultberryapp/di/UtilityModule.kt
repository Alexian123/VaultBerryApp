package com.alexianhentiu.vaultberryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.domain.utils.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.AESHandler
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.CryptographyHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {

    // Set up DataStore
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "com.alexianhentiu.vaultberryapp.user_settings"
    )

    @Provides
    @Singleton
    fun provideCryptoHandler(): CryptographyHandler = AESHandler("CBC/PKCS5Padding")

    @Provides
    @Singleton
    fun provideVaultGuardian(
        cryptoHandler: CryptographyHandler
    ): VaultGuardian = VaultGuardian(cryptoHandler)

    @Provides
    @Singleton
    fun providePasswordGenerator(): PasswordGenerator = PasswordGenerator()

    @Provides
    @Singleton
    fun provideInputValidator(): InputValidator = InputValidator()

    @Singleton
    @Provides
    fun providesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userDataStore
}