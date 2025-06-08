package com.alexianhentiu.vaultberryapp.data.di

import android.content.Context
import com.alexianhentiu.vaultberryapp.data.remote.SessionCookieJar
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidNetworkClientInitializer
import com.alexianhentiu.vaultberryapp.data.util.NetworkUtilsImpl
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.utils.NetworkClientInitializer
import com.alexianhentiu.vaultberryapp.domain.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkClientInitializer(
        @ApplicationContext context: Context,
        sessionCookieJar: SessionCookieJar,
        apiConfigRepository: ApiConfigRepository
    ): NetworkClientInitializer = AndroidNetworkClientInitializer(
        context,
        sessionCookieJar,
        apiConfigRepository
    )

    @Provides
    @Singleton
    fun provideApiService(
        androidNetworkClientInitializer: AndroidNetworkClientInitializer
    ): ApiService = androidNetworkClientInitializer.apiService

    @Provides
    @Singleton
    fun provideNetworkUtils(): NetworkUtils = NetworkUtilsImpl()
}