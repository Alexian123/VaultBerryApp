package com.alexianhentiu.vaultberryapp.data.api.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecoveryLoginRequestDTO(
    @Json(name = "email") val email: String,
    @Json(name = "recovery_password") val recoveryPassword: String,
    @Json(name = "otp") val otp: String
)
