package com.alexianhentiu.vaultberryapp.domain.model

data class PasswordPair(
    val regularPassword: String,
    val recoveryPassword: String
)
