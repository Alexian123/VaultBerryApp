package com.alexianhentiu.vaultberryapp.domain.model.entity

data class EncryptedVaultEntry(
    val title: String,
    val url: String?,
    val encryptedUsername: String?,
    val encryptedPassword: String?,
    val notes: String?
)