package com.alexianhentiu.vaultberryapp.data.model.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "account") val accountInfoDTO: AccountInfoDTO,
    @Json(name = "keychain") val keyChainDTO: KeyChainDTO,
    @Json(name = "passwords") val passwordPairDTO: PasswordPairDTO,
)
