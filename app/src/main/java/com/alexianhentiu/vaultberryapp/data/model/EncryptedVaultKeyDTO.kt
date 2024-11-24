package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EncryptedVaultKeyDTO(
    @Json(name = "salt") val salt: String,
    @Json(name = "vault_key") val vaultKey: String
)
