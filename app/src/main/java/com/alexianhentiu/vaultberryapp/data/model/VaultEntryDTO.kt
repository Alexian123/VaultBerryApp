package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json

data class VaultEntryDTO(
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String?,
    @Json(name = "encrypted_username") val encryptedUsername: String?,
    @Json(name = "encrypted_password") val encryptedPassword: String?,
    @Json(name = "notes") val notes: String?
)
