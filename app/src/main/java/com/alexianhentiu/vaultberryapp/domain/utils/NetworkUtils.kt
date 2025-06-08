package com.alexianhentiu.vaultberryapp.domain.utils

interface NetworkUtils {
    suspend fun isHostReachable(host: String, port: Int, timeoutMs: Int = 2000): Boolean
}