package com.alexianhentiu.vaultberryapp.data.api.dto.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeyChainDTO(
    @Json(name = "salt") val salt: String,
    @Json(name = "vault_key") val vaultKey: String,
    @Json(name = "recovery_key") val recoveryKey: String
)
