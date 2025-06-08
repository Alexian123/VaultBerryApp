package com.alexianhentiu.vaultberryapp.data.di

import android.content.ClipboardManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AppSettingsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AuthCredentialsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.ApiPreferencesDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.platform.biometric.AndroidBiometricAuthenticator
import com.alexianhentiu.vaultberryapp.data.platform.clipboard.AndroidClipboardHandler
import com.alexianhentiu.vaultberryapp.data.platform.crypto.AndroidSecureKeyHandler
import com.alexianhentiu.vaultberryapp.data.platform.datastore.apiPreferencesDataStore
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidUriParser
import com.alexianhentiu.vaultberryapp.data.platform.datastore.appSettingsDataStore
import com.alexianhentiu.vaultberryapp.data.platform.datastore.authCredentialsDataStore
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidAppInfoProvider
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidBase64Handler
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidStringResourceProvider
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidUriStreamProvider
import com.alexianhentiu.vaultberryapp.data.security.AESCipherProvider
import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.domain.security.BiometricAuthenticator
import com.alexianhentiu.vaultberryapp.domain.security.CipherCache
import com.alexianhentiu.vaultberryapp.domain.utils.UriParser
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import com.alexianhentiu.vaultberryapp.domain.utils.UriStreamProvider
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
    fun provideStringResourceProvider(
        @ApplicationContext context: Context,
    ): StringResourceProvider = AndroidStringResourceProvider(context)

    @Provides
    @Singleton
    @AppSettingsDataStoreQualifier
    fun provideAppSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.appSettingsDataStore

    @Provides
    @Singleton
    @AuthCredentialsDataStoreQualifier
    fun provideAuthCredentialsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.authCredentialsDataStore

    @Provides
    @Singleton
    @ApiPreferencesDataStoreQualifier
    fun provideApiPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.apiPreferencesDataStore

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

    @Provides
    @Singleton
    fun provideBase64Handler(): Base64Handler = AndroidBase64Handler()

    @Provides
    @Singleton
    fun provideUriStreamProvider(
        @ApplicationContext context: Context
    ): UriStreamProvider = AndroidUriStreamProvider(context)

    @Provides
    @Singleton
    fun provideBiometricAuthenticator(
        stringResourceProvider: StringResourceProvider,
        keyHandler: AndroidSecureKeyHandler,
        cipherProvider: AESCipherProvider,
        cipherCache: CipherCache,
        credentialsRepository: CredentialsRepository,
        @ApplicationContext context: Context
    ): BiometricAuthenticator = AndroidBiometricAuthenticator(
        stringResourceProvider,
        keyHandler,
        cipherProvider,
        cipherCache,
        credentialsRepository,
        context
    )
}