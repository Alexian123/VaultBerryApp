package com.alexianhentiu.vaultberryapp.domain.usecase.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class EncryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        decryptedVaultEntry: DecryptedVaultEntry,
        decryptedVaultKey: DecryptedVaultKey
    ): EncryptedVaultEntry {
        val encryptedUsername =
            vaultGuardian.exportField(decryptedVaultEntry.username, decryptedVaultKey)
        val encryptedPassword =
            vaultGuardian.exportField(decryptedVaultEntry.password, decryptedVaultKey)
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