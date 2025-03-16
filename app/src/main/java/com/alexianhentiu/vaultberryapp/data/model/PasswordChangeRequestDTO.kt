package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordChangeRequestDTO(
    @Json(name = "passwords") val passwordPair: PasswordPairDTO,
    @Json(name = "keychain") val keyChain: KeyChainDTO
)
