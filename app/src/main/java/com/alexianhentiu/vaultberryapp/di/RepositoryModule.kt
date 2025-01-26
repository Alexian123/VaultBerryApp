package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.repository.AccountRepositoryImpl
import com.alexianhentiu.vaultberryapp.data.repository.ModelConverter
import com.alexianhentiu.vaultberryapp.data.repository.UserRepositoryImpl
import com.alexianhentiu.vaultberryapp.data.repository.VaultEntryRepositoryImpl
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
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
    fun provideUserRepository(
        apiService: APIService,
        apiResponseHandler: APIResponseHandler,
        modelConverter: ModelConverter
    ): UserRepository = UserRepositoryImpl(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideVaultEntryRepository(
        apiService: APIService,
        apiResponseHandler: APIResponseHandler,
        modelConverter: ModelConverter
    ): VaultEntryRepository =
        VaultEntryRepositoryImpl(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideAccountRepository(
        apiService: APIService,
        apiResponseHandler: APIResponseHandler,
        modelConverter: ModelConverter
    ): AccountRepository = AccountRepositoryImpl(apiService, apiResponseHandler, modelConverter)

}