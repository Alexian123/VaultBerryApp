package com.alexianhentiu.vaultberryapp.data.platform.utils

import android.content.Context
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.SessionCookieJar
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.utils.NetworkClientInitializer
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Singleton
class AndroidNetworkClientInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionCookieJar: SessionCookieJar,
    private val apiConfigRepository: ApiConfigRepository
) : NetworkClientInitializer {
    private var _apiService: ApiService? = null

    val apiService: ApiService
        get() = _apiService ?: throw IllegalStateException("NetworkClient not initialized")

    override suspend fun initialize() {
        val x509Certificate = createX509Certificate()
        val trustManager = createTrustManager(x509Certificate)
        val sslContext = createSSLContext(trustManager)
        val loggingInterceptor = createLoggingInterceptor()
        val okHttpClient = createOkHttpClient(
            sessionCookieJar,
            trustManager,
            sslContext,
            loggingInterceptor
        )
        val baseUrl = apiConfigRepository.getUrl() ?: "https://127.0.0.1:5000/"
        val retrofit = createRetrofit(baseUrl, okHttpClient)
        _apiService = createApiService(retrofit)
    }


    private suspend fun createX509Certificate(
    ): X509Certificate {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val inputStream: InputStream = apiConfigRepository.getCertificateBytes()?.inputStream()
            ?: context.resources.openRawResource(R.raw.cert)
        val certificate: X509Certificate = certificateFactory
            .generateCertificate(inputStream) as X509Certificate
        inputStream.close()
        return certificate
    }

    private fun createTrustManager(
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

    private fun createSSLContext(
        trustManager: X509TrustManager
    ): SSLContext = SSLContext.getInstance("TLS")
        .apply { init(null, arrayOf(trustManager), null) }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    private fun createOkHttpClient(
        sessionCookieJar: SessionCookieJar,
        trustManager: X509TrustManager,
        sslContext: SSLContext,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(sessionCookieJar)
        .sslSocketFactory(sslContext.socketFactory, trustManager)
        .addInterceptor(loggingInterceptor)
        .build()

    private fun createRetrofit(
        baseUrl: String,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private fun createApiService(
        retrofit: Retrofit
    ): ApiService = retrofit.create(ApiService::class.java)
}