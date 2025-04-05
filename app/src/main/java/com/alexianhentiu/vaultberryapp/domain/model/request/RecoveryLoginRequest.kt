package com.alexianhentiu.vaultberryapp.domain.model.request

data class RecoveryLoginRequest(
    val email: String,
    val recoveryPassword: String,
    val otp: String
)
