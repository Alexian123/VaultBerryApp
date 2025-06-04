package com.alexianhentiu.vaultberryapp.domain.model

data class User(
    val accountInfo: AccountInfo,
    val keyChain: KeyChain,
    val passwordPair: PasswordPair,
    val noActivation: Boolean
)
