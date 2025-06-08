package com.alexianhentiu.vaultberryapp.domain.utils

interface NetworkClientInitializer {
    suspend fun initialize()
}