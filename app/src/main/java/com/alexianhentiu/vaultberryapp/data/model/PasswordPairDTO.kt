package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordPairDTO(
    @Json(name = "regular_password") val regularPassword: String,
    @Json(name = "recovery_password") val recoveryPassword: String
)
