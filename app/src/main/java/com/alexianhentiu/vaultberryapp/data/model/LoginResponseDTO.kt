package com.alexianhentiu.vaultberryapp.data.model

data class LoginResponseDTO(
    val salt: String,
    val vaultKey: String,
    val recoveryKey: String
)
