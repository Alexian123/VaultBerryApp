package com.alexianhentiu.vaultberryapp.domain.model.entity

data class User(
    val accountInfo: AccountInfo,
    val keyChain: KeyChain,
    val passwordPair: PasswordPair
)
