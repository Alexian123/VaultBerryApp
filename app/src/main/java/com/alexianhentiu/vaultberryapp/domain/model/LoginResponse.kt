package com.alexianhentiu.vaultberryapp.domain.model

data class LoginResponse(
    val salt: String,
    val vaultKey: String,
    val recoveryKey: String
)
