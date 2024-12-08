package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class DecryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        encryptedVaultEntry: EncryptedVaultEntry,
        decryptedVaultKey: DecryptedVaultKey
    ): DecryptedVaultEntry {
        val decryptedUsername =
            vaultGuardian.importField(encryptedVaultEntry.encryptedUsername, decryptedVaultKey)
        val decryptedPassword =
            vaultGuardian.importField(encryptedVaultEntry.encryptedPassword, decryptedVaultKey)
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