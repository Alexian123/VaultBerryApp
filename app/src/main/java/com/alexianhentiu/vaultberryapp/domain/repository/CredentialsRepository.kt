package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials

interface CredentialsRepository {

    fun storeCredentials(encryptedCredentials: EncryptedAuthCredentials)

    fun storeEncryptedPassword(encryptedData: ByteArray, iv: ByteArray)

    fun storeEmail(email: String)

    fun getCredentials(): EncryptedAuthCredentials?

    fun hasStoredCredentials(): Boolean

    fun clearStoredCredentials()

}