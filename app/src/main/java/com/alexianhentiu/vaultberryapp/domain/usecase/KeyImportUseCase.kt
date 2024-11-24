package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class KeyImportUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        password: String,
        encryptedVaultKey: EncryptedVaultKey
    ): DecryptedVaultKey {
        return vaultGuardian.importExistingVaultKey(password, encryptedVaultKey)
    }
}