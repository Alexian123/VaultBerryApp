package com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class EncryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        decryptedVaultEntry: DecryptedVaultEntry,
        decryptedKey: DecryptedKey
    ): ActionResult<EncryptedVaultEntry> {
        try {
            val encryptedUsername =
                vaultGuardian.encryptField(decryptedVaultEntry.username, decryptedKey)
            val encryptedPassword =
                vaultGuardian.encryptField(decryptedVaultEntry.password, decryptedKey)
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
            return ActionResult.Error(
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Entry encryption failed: ${e.message}"
            )
        }
    }
}