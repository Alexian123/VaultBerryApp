package com.alexianhentiu.vaultberryapp.data.remote.model.request

import com.alexianhentiu.vaultberryapp.data.remote.model.AccountInfoDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AccountInfoChangeRequestDTO (
    @Json(name = "account") val account: AccountInfoDTO,
    @Json(name = "no_activation_required") val noActivation: Boolean
)