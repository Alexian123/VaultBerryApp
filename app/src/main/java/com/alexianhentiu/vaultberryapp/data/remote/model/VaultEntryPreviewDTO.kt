package com.alexianhentiu.vaultberryapp.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VaultEntryPreviewDTO(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String
)