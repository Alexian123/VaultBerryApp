package com.alexianhentiu.vaultberryapp.data.di

import android.content.ClipboardManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AppSettingsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AuthCredentialsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.platform.clipboard.AndroidClipboardHandler
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidUriParser
import com.alexianhentiu.vaultberryapp.data.platform.datastore.appSettingsDataStore
import com.alexianhentiu.vaultberryapp.data.platform.datastore.authCredentialsDataStore
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidAppInfoProvider
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidBase64Handler
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidStringResourceProvider
import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler
import com.alexianhentiu.vaultberryapp.domain.utils.UriParser
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
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
    @AppSettingsDataStoreQualifier
    fun provideAppSettingsDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.appSettingsDataStore

    @Provides
    @Singleton
    @AuthCredentialsDataStoreQualifier
    fun provideAuthCredentialsDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.authCredentialsDataStore

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
    fun provideStringResourceProvider(
        @ApplicationContext context: Context,
    ): StringResourceProvider = AndroidStringResourceProvider(context)

    @Provides
    @Singleton
    fun provideBase64Handler(): Base64Handler = AndroidBase64Handler()
}