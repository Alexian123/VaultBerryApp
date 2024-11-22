package com.alexianhentiu.vaultberryapp.domain.model

data class User(
    val email: String,
    val password: String,
    val salt: String,
    val vaultKey: String,
    val recoveryKey: String,
    val firstName: String?,
    val lastName: String?
)
