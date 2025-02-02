package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountDTO(
    @Json(name = "email") val email: String,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?
)
