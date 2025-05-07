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
            val encryptedVaultEntry = EncryptedVaultEntry(
                lastModified = decryptedVaultEntry.lastModified,
                title = decryptedVaultEntry.title,
                url = if (decryptedVaultEntry.url.isEmpty()) null else decryptedVaultEntry.url,
                encryptedUsername =
                    if (decryptedVaultEntry.username.isEmpty()) null
                    else vaultGuardian.encryptField(decryptedVaultEntry.username, decryptedKey),
                encryptedPassword =
                    if (decryptedVaultEntry.password.isEmpty()) null
                    else vaultGuardian.encryptField(decryptedVaultEntry.password, decryptedKey),
                notes = if (decryptedVaultEntry.notes.isEmpty()) null else decryptedVaultEntry.notes
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