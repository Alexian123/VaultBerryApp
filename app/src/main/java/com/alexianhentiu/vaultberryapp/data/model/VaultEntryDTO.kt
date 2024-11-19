package com.alexianhentiu.vaultberryapp.data.model

data class VaultEntryDTO(
    val title: String,
    val url: String?,
    val encryptedUsername: String?,
    val encryptedPassword: String?,
    val notes: String?
)
