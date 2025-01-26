package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecoveryKeyDTO(
    @Json(name = "salt") val salt: String,
    @Json(name = "recovery_key") val key: String
)
