package com.alexianhentiu.vaultberryapp.domain.utils.cryptography

import javax.crypto.SecretKey

interface CryptographyHandler {

    fun generateKey(): SecretKey

    fun encrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray

    fun decrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray

    fun deriveKeyFromPassword(password: String, salt: ByteArray): SecretKey
}