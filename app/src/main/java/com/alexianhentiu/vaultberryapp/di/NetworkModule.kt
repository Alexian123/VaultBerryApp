package com.alexianhentiu.vaultberryapp.di

import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.api.SessionCookieJar
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    //fun provideBaseUrl(): String = "http://10.0.2.2:5000/"        /* emulator */
    fun provideBaseUrl(): String = "http://192.168.1.131:5000/"   /* physical device */

    @Provides
    @Singleton
    fun provideSessionCookieJar(): SessionCookieJar = SessionCookieJar()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        sessionCookieJar: SessionCookieJar
    ): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(sessionCookieJar)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): APIService = retrofit.create(APIService::class.java)

    @Provides
    @Singleton
    fun provideAPIResponseHandler(): APIResponseHandler = APIResponseHandler()

    @Provides
    @Singleton
    fun provideModelConverter(): ModelConverter = ModelConverter()
}