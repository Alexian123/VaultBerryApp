package com.alexianhentiu.vaultberryapp.domain.usecase.security

import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class EncryptVaultKeyUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(password: String): EncryptedVaultKey {
        return vaultGuardian.exportNewVaultKey(password)
    }
}