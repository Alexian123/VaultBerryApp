package com.alexianhentiu.vaultberryapp.domain.model

data class NewEncryptedVaultEntry(
    val title: String,
    val url: String?,
    val encryptedUsername: String,
    val encryptedPassword: String,
    val notes: String?
)
