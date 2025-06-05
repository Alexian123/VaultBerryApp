package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials

interface CredentialsRepository {

    suspend fun storeCredentials(encryptedCredentials: EncryptedAuthCredentials)

    suspend fun storeEncryptedPassword(encryptedData: ByteArray, iv: ByteArray)

    suspend fun storeEmail(email: String)

    suspend fun getCredentials(): EncryptedAuthCredentials?

    suspend fun hasStoredCredentials(): Boolean

    suspend fun clearStoredCredentials()

}