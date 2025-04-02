package com.alexianhentiu.vaultberryapp.domain.model.entity

data class PasswordPair(
    val regularPassword: String,
    val recoveryPassword: String
)
