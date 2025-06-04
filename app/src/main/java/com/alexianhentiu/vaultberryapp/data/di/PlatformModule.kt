package com.alexianhentiu.vaultberryapp.data.di

import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alexianhentiu.vaultberryapp.data.platform.biometric.AndroidBiometricAuthenticator
import com.alexianhentiu.vaultberryapp.data.platform.crypto.AndroidEncryptDecryptProcessor
import com.alexianhentiu.vaultberryapp.data.platform.clipboard.AndroidClipboardHandler
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidUriParser
import com.alexianhentiu.vaultberryapp.data.platform.datastore.userDataStore
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidAppInfoProvider
import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler
import com.alexianhentiu.vaultberryapp.domain.utils.UriParser
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlatformModule {

    @Provides
    @Singleton
    fun provideBiometricAuthManager(
        cryptoManager: AndroidEncryptDecryptProcessor,
        credentialsRepository: CredentialsRepository,
        @ApplicationContext context: Context
    ): AndroidBiometricAuthenticator =
        AndroidBiometricAuthenticator(cryptoManager, credentialsRepository, context)

    @Singleton
    @Provides
    fun providesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userDataStore

    @Provides
    @Singleton
    fun provideCredentialsSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(
        "auth_credentials",
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideUriParser(): UriParser = AndroidUriParser()

    @Provides
    @Singleton
    fun provideClipboardManager(
        @ApplicationContext context: Context,
    ): ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @Singleton
    fun provideClipboardHandler(
        clipboardManager: ClipboardManager
    ): ClipboardHandler = AndroidClipboardHandler(clipboardManager)

    @Provides
    @Singleton
    fun provideAppInfoProvider(
        @ApplicationContext context: Context,
    ): AppInfoProvider = AndroidAppInfoProvider(context)
}