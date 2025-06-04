package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import javax.inject.Inject

class EncryptVaultEntryUseCase @Inject constructor(
    private val handler: VaultSecurityHandler
) {

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
                    else handler.encryptField(decryptedVaultEntry.username, decryptedKey),
                encryptedPassword =
                    if (decryptedVaultEntry.password.isEmpty()) null
                    else handler.encryptField(decryptedVaultEntry.password, decryptedKey),
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