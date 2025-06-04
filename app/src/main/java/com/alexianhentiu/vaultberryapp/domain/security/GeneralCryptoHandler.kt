package com.alexianhentiu.vaultberryapp.domain.security

interface GeneralCryptoHandler {

    fun generateKey(): ByteArray

    fun encrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray

    fun decrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray

    fun deriveKeyFromPassword(password: String, salt: ByteArray): ByteArray
}