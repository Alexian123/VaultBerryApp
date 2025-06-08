package com.alexianhentiu.vaultberryapp.domain.security

interface GeneralCryptoHandler {

    val algorithm: String
    val mode: String
    val keySizeBits: Int
    val ivLengthBytes: Int
    val tagLengthBits: Int

    fun generateKey(): ByteArray

    fun generateIV(): ByteArray

    fun generateSalt(lengthBytes: Int): ByteArray

    fun encrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray

    fun decrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray
}