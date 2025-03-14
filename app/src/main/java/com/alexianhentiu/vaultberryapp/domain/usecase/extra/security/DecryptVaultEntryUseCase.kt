package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class DecryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        encryptedVaultEntry: EncryptedVaultEntry,
        decryptedKey: DecryptedKey
    ): ActionResult<DecryptedVaultEntry> {
        try {
            val decryptedUsername =
                vaultGuardian.decryptField(encryptedVaultEntry.encryptedUsername, decryptedKey.key)
            val decryptedPassword =
                vaultGuardian.decryptField(encryptedVaultEntry.encryptedPassword, decryptedKey.key)
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