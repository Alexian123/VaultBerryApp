package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class EncryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        decryptedVaultEntry: DecryptedVaultEntry,
        decryptedKey: ByteArray
    ): UseCaseResult<EncryptedVaultEntry> {
        try {
            val encryptedVaultEntry = EncryptedVaultEntry(
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
            return UseCaseResult.Success(encryptedVaultEntry)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.ENTRY_ENCRYPTION_FAILURE,
                "Vault Guardian",
                e.message ?: "Unknown error"
            )
        }
    }
}