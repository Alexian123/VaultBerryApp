package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class GenerateKeyChainUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(password: String, recoveryPassword: String): KeyChain {
        return vaultGuardian.generateKeyChain(password, recoveryPassword)
    }
}