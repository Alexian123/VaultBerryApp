package com.alexianhentiu.vaultberryapp.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "account_info") val accountInfoDTO: AccountInfoDTO,
    @Json(name = "keychain") val keyChainDTO: KeyChainDTO,
    @Json(name = "passwords") val passwordPairDTO: PasswordPairDTO,
    @Json(name = "no_activation_required") val noActivation: Boolean
)
