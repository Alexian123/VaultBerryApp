package com.alexianhentiu.vaultberryapp.data.api.dto.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "account_info") val accountInfoDTO: AccountInfoDTO,
    @Json(name = "keychain") val keyChainDTO: KeyChainDTO,
    @Json(name = "passwords") val passwordPairDTO: PasswordPairDTO,
)
