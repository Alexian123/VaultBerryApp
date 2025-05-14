package com.alexianhentiu.vaultberryapp.data.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VaultSearchRequest(
    @Json(name = "keywords") val keywords: List<String>
)
