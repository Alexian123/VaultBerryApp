package com.alexianhentiu.vaultberryapp.data.api.model.request

import com.alexianhentiu.vaultberryapp.data.api.model.entity.PasswordPairDTO
import com.alexianhentiu.vaultberryapp.data.api.model.entity.KeyChainDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordChangeRequestDTO(
    @Json(name = "passwords") val passwordPair: PasswordPairDTO,
    @Json(name = "keychain") val keyChain: KeyChainDTO,
    @Json(name = "re_encrypt") val reEncrypt: Boolean
)
