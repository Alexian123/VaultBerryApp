package com.alexianhentiu.vaultberryapp.application.di

import com.alexianhentiu.vaultberryapp.application.usecase.apiConfig.ClearApiCertificateUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.apiConfig.GetApiCertificateUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.apiConfig.GetApiUrlUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.apiConfig.PingApiUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.apiConfig.SetApiCertificateUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.apiConfig.SetApiUrlUseCaseImpl
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.ClearApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.GetApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.GetApiUrlUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.PingApiUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.SetApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.SetApiUrlUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.NetworkUtils
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ApiConfigUseCaseModule {

    @Provides
    fun provideSetApiCertificateUseCase(
        apiConfigRepository: ApiConfigRepository,
        stringResourceProvider: StringResourceProvider
    ): SetApiCertificateUseCase = SetApiCertificateUseCaseImpl(
        apiConfigRepository,
        stringResourceProvider
    )

    @Provides
    fun provideGetApiCertificateUseCase(
        apiConfigRepository: ApiConfigRepository,
        stringResourceProvider: StringResourceProvider
    ): GetApiCertificateUseCase = GetApiCertificateUseCaseImpl(
        apiConfigRepository,
        stringResourceProvider
    )

    @Provides
    fun provideSetApiUrlUseCase(
        apiConfigRepository: ApiConfigRepository,
        stringResourceProvider: StringResourceProvider
    ): SetApiUrlUseCase = SetApiUrlUseCaseImpl(
        apiConfigRepository,
        stringResourceProvider
    )

    @Provides
    fun provideGetApiUrlUseCase(
        apiConfigRepository: ApiConfigRepository,
        stringResourceProvider: StringResourceProvider
    ): GetApiUrlUseCase = GetApiUrlUseCaseImpl(
        apiConfigRepository,
        stringResourceProvider
    )

    @Provides
    fun providePingApiUseCase(
        networkUtils: NetworkUtils,
        stringResourceProvider: StringResourceProvider
    ): PingApiUseCase = PingApiUseCaseImpl(
        networkUtils,
        stringResourceProvider
    )

    @Provides
    fun provideClearApiCertificateUseCase(
        apiConfigRepository: ApiConfigRepository,
        stringResourceProvider: StringResourceProvider
    ): ClearApiCertificateUseCase = ClearApiCertificateUseCaseImpl(
        apiConfigRepository,
        stringResourceProvider
    )
}