package com.alexianhentiu.vaultberryapp.domain.repository

interface ApiConfigRepository {
    suspend fun getUrl(): String?
    suspend fun storeUrl(url: String)
    suspend fun getCertificateBytes(): ByteArray?
    suspend fun storeCertificateBytes(certificateBytes: ByteArray)
    suspend fun clearCertificate()
}