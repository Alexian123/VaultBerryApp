package com.alexianhentiu.vaultberryapp.domain.model

data class User(
    val account: Account,
    val keyChain: KeyChain,
    val password: String
)
