package com.alexianhentiu.vaultberryapp.data.api.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TotpResponseDTO(
    @Json(name = "provisioning_uri") val provisioningUri: String,
    @Json(name = "qrcode") val qrCode: String
)
