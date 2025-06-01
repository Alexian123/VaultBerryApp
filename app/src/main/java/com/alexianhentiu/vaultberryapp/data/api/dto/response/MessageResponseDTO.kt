package com.alexianhentiu.vaultberryapp.data.api.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageResponseDTO(
    @Json(name = "message") val message: String,
)
