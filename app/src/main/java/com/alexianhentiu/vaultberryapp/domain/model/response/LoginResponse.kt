package com.alexianhentiu.vaultberryapp.domain.model.response

import com.alexianhentiu.vaultberryapp.domain.model.entity.KeyChain

data class LoginResponse(
    val serverMessage: String,
    val keyChain: KeyChain?
)
