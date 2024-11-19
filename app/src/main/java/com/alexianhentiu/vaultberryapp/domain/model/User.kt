package com.alexianhentiu.vaultberryapp.domain.model

data class User(
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?
)
