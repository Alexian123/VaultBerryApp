package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginCredentialsDTO(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
