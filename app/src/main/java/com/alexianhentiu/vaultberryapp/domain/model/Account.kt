package com.alexianhentiu.vaultberryapp.domain.model

data class Account(
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
)
