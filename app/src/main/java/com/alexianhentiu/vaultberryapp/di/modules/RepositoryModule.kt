package com.alexianhentiu.vaultberryapp.di.modules

import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.repository.AccountRepositoryImpl
import com.alexianhentiu.vaultberryapp.data.repository.AuthRepositoryImpl
import com.alexianhentiu.vaultberryapp.data.repository.VaultRepositoryImpl
import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
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
        apiService: APIService,
        apiResponseHandler: APIResponseHandler,
        modelConverter: ModelConverter
    ): AuthRepository = AuthRepositoryImpl(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideVaultRepository(
        apiService: APIService,
        apiResponseHandler: APIResponseHandler,
        modelConverter: ModelConverter
    ): VaultRepository =
        VaultRepositoryImpl(apiService, apiResponseHandler, modelConverter)

    @Provides
    @Singleton
    fun provideAccountRepository(
        apiService: APIService,
        apiResponseHandler: APIResponseHandler,
        modelConverter: ModelConverter
    ): AccountRepository = AccountRepositoryImpl(apiService, apiResponseHandler, modelConverter)

}