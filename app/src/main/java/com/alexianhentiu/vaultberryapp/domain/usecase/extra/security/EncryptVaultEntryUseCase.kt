package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class EncryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        decryptedVaultEntry: DecryptedVaultEntry,
        decryptedKey: DecryptedKey
    ): ActionResult<EncryptedVaultEntry> {
        try {
            val encryptedUsername =
                vaultGuardian.encryptField(decryptedVaultEntry.username, decryptedKey.key)
            val encryptedPassword =
                vaultGuardian.encryptField(decryptedVaultEntry.password, decryptedKey.key)
            val encryptedVaultEntry = EncryptedVaultEntry(
                decryptedVaultEntry.timestamp,
                decryptedVaultEntry.title,
                decryptedVaultEntry.url,
                encryptedUsername,
                encryptedPassword,
                decryptedVaultEntry.notes
            )
            return ActionResult.Success(encryptedVaultEntry)
        } catch (e: Exception) {
            return ActionResult.Error("Entry encryption failed: ${e.message}")
        }
    }
}