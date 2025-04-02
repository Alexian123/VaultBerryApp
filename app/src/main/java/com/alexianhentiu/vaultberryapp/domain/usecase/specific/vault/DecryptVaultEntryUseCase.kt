package com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class DecryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        encryptedVaultEntry: EncryptedVaultEntry,
        decryptedKey: DecryptedKey
    ): ActionResult<DecryptedVaultEntry> {
        try {
            val decryptedUsername =
                vaultGuardian.decryptField(encryptedVaultEntry.encryptedUsername, decryptedKey)
            val decryptedPassword =
                vaultGuardian.decryptField(encryptedVaultEntry.encryptedPassword, decryptedKey)
            val decryptedVaultEntry = DecryptedVaultEntry(
                encryptedVaultEntry.timestamp,
                encryptedVaultEntry.title,
                encryptedVaultEntry.url,
                decryptedUsername,
                decryptedPassword,
                encryptedVaultEntry.notes
            )
            return ActionResult.Success(decryptedVaultEntry)
        } catch (e: Exception) {
            return ActionResult.Error(
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Entry decryption failed: ${e.message}"
            )
        }
    }
}