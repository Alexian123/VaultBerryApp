package com.alexianhentiu.vaultberryapp.data.model

data class UserDTO(
    val email: String,
    val password: String,
    val salt: String,
    val vaultKey: String,
    val recoveryKey: String,
    val firstName: String?,
    val lastName: String?
)
