package com.alexianhentiu.vaultberryapp.domain.model

data class RecoveryKey(
    val oneTimePassword: String,
    val salt: String,
    val key: String
)
