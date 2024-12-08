package com.alexianhentiu.vaultberryapp.domain.model

data class DecryptedVaultEntry(
    val timestamp: Long,
    val title: String,
    val url: String,
    val username: String,
    val password: String,
    val notes: String
)
