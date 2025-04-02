package com.alexianhentiu.vaultberryapp.domain.model.request

import com.alexianhentiu.vaultberryapp.domain.model.entity.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.entity.KeyChain

data class PasswordChangeRequest(
    val passwordPair: PasswordPair,
    val keyChain: KeyChain
)
