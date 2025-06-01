package com.alexianhentiu.vaultberryapp.data.api.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooleanResponseDTO(
    @Json(name = "enabled") val enabled: Boolean
)
