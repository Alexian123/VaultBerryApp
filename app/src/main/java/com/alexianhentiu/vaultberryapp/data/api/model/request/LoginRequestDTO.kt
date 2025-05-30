package com.alexianhentiu.vaultberryapp.data.api.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDTO(
    @Json(name = "email") val email: String,
    @Json(name = "client_message") val clientMessage: String,
    @Json(name = "code") val totpCode: String?
)
