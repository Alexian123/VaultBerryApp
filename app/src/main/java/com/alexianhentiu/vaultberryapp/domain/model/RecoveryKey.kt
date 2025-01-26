package com.alexianhentiu.vaultberryapp.domain.model

data class RecoveryKey(
    val salt: String,
    val key: String
)
