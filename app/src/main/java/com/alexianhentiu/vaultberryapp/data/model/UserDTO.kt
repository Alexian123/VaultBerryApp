package com.alexianhentiu.vaultberryapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "account") val accountDTO: AccountDTO,
    @Json(name = "keychain") val keyChainDTO: KeyChainDTO,
    @Json(name = "passwords") val passwordPairDTO: PasswordPairDTO,
)
