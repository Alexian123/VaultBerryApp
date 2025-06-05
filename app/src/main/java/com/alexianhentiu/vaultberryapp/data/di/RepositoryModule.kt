package com.alexianhentiu.vaultberryapp.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AppSettingsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AuthCredentialsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.data.remote.ApiResponseHandler
import com.alexianhentiu.vaultberryapp.data.remote.ModelConverter
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.data.repository.local.DataStoreCredentialsRepository
import com.alexianhentiu.vaultberryapp.data.repository.local.DataStoreSettingsRepository
import com.alexianhentiu.vaultberryapp.data.repository.remote.RemoteAccountRepository
import com.alexianhentiu.vaultberryapp.data.repository.remote.RemoteAuthRepository
import com.alexianhentiu.vaultberryapp.data.repository.remote.RemoteVaultRepository
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.domain.repository.SettingsRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        apiResponseHandler: ApiResponseHandler,
        modelConverter: ModelConverter
    ): AuthRepository = RemoteAuthRepository(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideVaultRepository(
        apiService: ApiService,
        apiResponseHandler: ApiResponseHandler,
        modelConverter: ModelConverter
    ): VaultRepository =
        RemoteVaultRepository(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideAccountRepository(
        apiService: ApiService,
        apiResponseHandler: ApiResponseHandler,
        modelConverter: ModelConverter
    ): AccountRepository = RemoteAccountRepository(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @AppSettingsDataStoreQualifier dataStore: DataStore<Preferences>
    ): SettingsRepository = DataStoreSettingsRepository(dataStore)

    @Provides
    @Singleton
    fun provideCredentialsRepository(
        @AuthCredentialsDataStoreQualifier dataStore: DataStore<Preferences>,
        base64Handler: Base64Handler
    ): CredentialsRepository = DataStoreCredentialsRepository(dataStore, base64Handler)
}