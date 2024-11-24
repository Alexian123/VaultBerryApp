package com.alexianhentiu.vaultberryapp.domain.model

data class EncryptedVaultKey(
    val salt: String,
    val ivAndKey: String
)