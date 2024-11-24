package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class KeyExportUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(password: String): EncryptedVaultKey {
        return vaultGuardian.exportNewVaultKey(password)
    }
}