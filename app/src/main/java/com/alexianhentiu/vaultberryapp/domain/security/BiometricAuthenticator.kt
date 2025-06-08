package com.alexianhentiu.vaultberryapp.domain.security

import com.alexianhentiu.vaultberryapp.domain.common.BiometricStatus
import com.alexianhentiu.vaultberryapp.domain.model.AuthCredentials
import com.alexianhentiu.vaultberryapp.domain.model.CipherContext
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials

interface BiometricAuthenticator {

    suspend fun hasStoredCredentials(): Boolean

    suspend fun clearStoredCredentials()

    fun getEncryptionContext(): CipherContext

    fun getDecryptionContext(iv: ByteArray): CipherContext

    fun performEncryption(
        context: CipherContext,
        password: String,
        email: String
    ): EncryptedAuthCredentials

    fun performDecryption(
        context: CipherContext,
        encryptedData: EncryptedAuthCredentials
    ): AuthCredentials

    suspend fun getCredentials(): EncryptedAuthCredentials?

    suspend fun storeCredentials(encryptedAuthCredentials: EncryptedAuthCredentials)

    fun isBiometricAvailable(): BiometricStatus
}