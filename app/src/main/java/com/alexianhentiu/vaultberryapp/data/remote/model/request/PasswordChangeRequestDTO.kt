package com.alexianhentiu.vaultberryapp.data.remote.model.request

import com.alexianhentiu.vaultberryapp.data.remote.model.PasswordPairDTO
import com.alexianhentiu.vaultberryapp.data.remote.model.KeyChainDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordChangeRequestDTO(
    @Json(name = "passwords") val passwordPair: PasswordPairDTO,
    @Json(name = "keychain") val keyChain: KeyChainDTO,
    @Json(name = "re_encrypt") val reEncrypt: Boolean
)
