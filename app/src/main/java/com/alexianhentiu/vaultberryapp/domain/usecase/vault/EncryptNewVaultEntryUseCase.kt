package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.NewDecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.NewEncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class EncryptNewVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        newDecryptedVaultEntry: NewDecryptedVaultEntry,
        decryptedVaultKey: DecryptedVaultKey
    ): NewEncryptedVaultEntry {
        val encryptedUsername =
            vaultGuardian.exportField(newDecryptedVaultEntry.username, decryptedVaultKey)
        val encryptedPassword =
            vaultGuardian.exportField(newDecryptedVaultEntry.password, decryptedVaultKey)
        return NewEncryptedVaultEntry(
            newDecryptedVaultEntry.title,
            newDecryptedVaultEntry.url,
            encryptedUsername,
            encryptedPassword,
            newDecryptedVaultEntry.notes
        )
    }
}