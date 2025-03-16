package com.alexianhentiu.vaultberryapp.domain.model

data class PasswordChangeRequest(
    val passwordPair: PasswordPair,
    val keyChain: KeyChain
)
