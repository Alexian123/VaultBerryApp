package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json

data class LoginResponseDTO(
    @Json(name = "salt") val salt: String,
    @Json(name = "vault_key") val vaultKey: String,
    @Json(name = "recovery_key") val recoveryKey: String
)
