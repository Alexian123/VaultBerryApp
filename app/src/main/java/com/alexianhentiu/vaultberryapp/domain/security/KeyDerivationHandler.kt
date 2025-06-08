package com.alexianhentiu.vaultberryapp.domain.security

interface KeyDerivationHandler {
    val keySizeBits: Int
    val kdfAlgorithm: String
    val iterationCount: Int

    fun deriveKeyFromPassword(password: String, salt: ByteArray): ByteArray
}