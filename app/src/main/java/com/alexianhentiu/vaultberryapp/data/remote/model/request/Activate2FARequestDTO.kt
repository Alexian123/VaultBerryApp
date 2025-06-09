package com.alexianhentiu.vaultberryapp.data.remote.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Activate2FARequestDTO(
    @Json(name = "totp_code") val totpCode: String
)
