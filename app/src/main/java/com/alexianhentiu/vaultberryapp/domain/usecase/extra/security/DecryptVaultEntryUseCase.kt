package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class DecryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        encryptedVaultEntry: EncryptedVaultEntry,
        decryptedKey: DecryptedKey
    ): DecryptedVaultEntry {
        val decryptedUsername =
            vaultGuardian.decryptField(encryptedVaultEntry.encryptedUsername, decryptedKey.key)
        val decryptedPassword =
            vaultGuardian.decryptField(encryptedVaultEntry.encryptedPassword, decryptedKey.key)
        return DecryptedVaultEntry(
            encryptedVaultEntry.timestamp,
            encryptedVaultEntry.title,
            encryptedVaultEntry.url,
            decryptedUsername,
            decryptedPassword,
            encryptedVaultEntry.notes
        )
    }
}