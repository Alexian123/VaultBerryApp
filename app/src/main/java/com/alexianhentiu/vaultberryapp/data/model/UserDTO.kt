package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "salt") val salt: String,
    @Json(name = "vault_key") val vaultKey: String,
    @Json(name = "recovery_key") val recoveryKey: String,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?
)
