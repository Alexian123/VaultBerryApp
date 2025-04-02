package com.alexianhentiu.vaultberryapp.domain.model.request

data class LoginRequest(
    val email: String,
    val clientMessage: String,
    val totpCode: String?
)
