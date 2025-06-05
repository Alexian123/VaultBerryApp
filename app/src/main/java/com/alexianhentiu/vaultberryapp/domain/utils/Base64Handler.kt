package com.alexianhentiu.vaultberryapp.domain.utils

interface Base64Handler {
    fun encode(bytes: ByteArray): String
    fun decode(encoded: String): ByteArray
}