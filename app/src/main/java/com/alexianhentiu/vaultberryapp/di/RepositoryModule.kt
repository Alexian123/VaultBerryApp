package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.repository.UserRepositoryImpl
import com.alexianhentiu.vaultberryapp.data.repository.VaultEntryRepositoryImpl
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
    fun provideUserRepository(apiService: APIService, apiResponseHandler: APIResponseHandler):
            UserRepository = UserRepositoryImpl(apiService, apiResponseHandler)

    @Provides
    @Singleton
    fun provideVaultEntryRepository(apiService: APIService, apiResponseHandler: APIResponseHandler):
            VaultEntryRepository = VaultEntryRepositoryImpl(apiService, apiResponseHandler)
}