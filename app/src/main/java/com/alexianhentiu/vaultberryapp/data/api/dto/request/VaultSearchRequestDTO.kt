package com.alexianhentiu.vaultberryapp.data.api.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VaultSearchRequestDTO(
    @Json(name = "keywords") val keywords: List<String>
)
