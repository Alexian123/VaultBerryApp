package com.alexianhentiu.vaultberryapp.domain.usecase.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class EncryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        decryptedVaultEntry: DecryptedVaultEntry,
        decryptedKey: DecryptedKey
    ): EncryptedVaultEntry {
        val encryptedUsername =
            vaultGuardian.encryptField(decryptedVaultEntry.username, decryptedKey.key)
        val encryptedPassword =
            vaultGuardian.encryptField(decryptedVaultEntry.password, decryptedKey.key)
        return EncryptedVaultEntry(
            decryptedVaultEntry.timestamp,
            decryptedVaultEntry.title,
            decryptedVaultEntry.url,
            encryptedUsername,
            encryptedPassword,
            decryptedVaultEntry.notes
        )
    }
}