package com.alexianhentiu.vaultberryapp.di

import android.content.Context
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.utils.SessionCookieJar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = "https://192.168.1.129:8443/"

    @Provides
    @Singleton
    fun provideSessionCookieJar(): SessionCookieJar = SessionCookieJar()


    @Provides
    @Singleton
    fun provideCertificate(
        @ApplicationContext context: Context
    ): X509Certificate {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val inputStream: InputStream = context.resources.openRawResource(R.raw.cert)
        val certificate: X509Certificate = certificateFactory
            .generateCertificate(inputStream) as X509Certificate
        inputStream.close()
        return certificate
    }

    @Provides
    @Singleton
    fun provideTrustManager(
        certificate: X509Certificate
    ): X509TrustManager {
        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType).apply {
            load(null, null)
            setCertificateEntry("ca", certificate)
        }

        // Create a TrustManager that trusts the CAs from the KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
            init(keyStore)
        }

        return trustManagerFactory.trustManagers
            .first { it is X509TrustManager } as X509TrustManager
    }

    @Provides
    @Singleton
    fun provideSSLContext(
        trustManager: X509TrustManager
    ): SSLContext = SSLContext.getInstance("TLS")
        .apply { init(null, arrayOf(trustManager), null) }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        sessionCookieJar: SessionCookieJar,
        trustManager: X509TrustManager,
        sslContext: SSLContext,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(sessionCookieJar)
        .sslSocketFactory(sslContext.socketFactory, trustManager)
        .addInterceptor(loggingInterceptor)
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
}