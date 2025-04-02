package com.alexianhentiu.vaultberryapp.data.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EncryptedVaultEntryDTO(
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String,
    @Json(name = "encrypted_username") val encryptedUsername: String,
    @Json(name = "encrypted_password") val encryptedPassword: String,
    @Json(name = "notes") val notes: String
)
