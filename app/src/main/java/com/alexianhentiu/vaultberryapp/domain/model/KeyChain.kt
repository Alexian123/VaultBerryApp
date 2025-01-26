package com.alexianhentiu.vaultberryapp.domain.model

data class KeyChain(
    val salt: String,
    val vaultKey: String,
    val recoveryKey: String
)
