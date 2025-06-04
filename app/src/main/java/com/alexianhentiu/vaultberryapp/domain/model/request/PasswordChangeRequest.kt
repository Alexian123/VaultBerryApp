package com.alexianhentiu.vaultberryapp.domain.model.request

import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain

data class PasswordChangeRequest(
    val passwordPair: PasswordPair,
    val keyChain: KeyChain,
    val reEncrypt: Boolean
)
