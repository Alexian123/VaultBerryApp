package com.alexianhentiu.vaultberryapp.data.api.model.response

import com.alexianhentiu.vaultberryapp.data.api.model.entity.KeyChainDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseDTO(
    @Json(name = "server_message") val serverMessage: String,
    @Json(name = "keychain") val keyChain: KeyChainDTO?
)
