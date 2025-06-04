package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import javax.inject.Inject

class DecryptVaultEntryUseCase @Inject constructor(
    private val handler: VaultSecurityHandler
) {

    operator fun invoke(
        encryptedVaultEntry: EncryptedVaultEntry,
        decryptedKey: ByteArray
    ): UseCaseResult<DecryptedVaultEntry> {
        try {
            var decryptedUsername = ""
            if (encryptedVaultEntry.encryptedUsername != null) {
                decryptedUsername = handler.decryptField(
                    encryptedVaultEntry.encryptedUsername,
                    decryptedKey
                )
            }

            var decryptedPassword = ""
            if (encryptedVaultEntry.encryptedPassword != null) {
                decryptedPassword = handler.decryptField(
                    encryptedVaultEntry.encryptedPassword,
                    decryptedKey
                )
            }

            val decryptedVaultEntry = DecryptedVaultEntry(
                title = encryptedVaultEntry.title,
                url = encryptedVaultEntry.url ?: "",
                username = decryptedUsername,
                password = decryptedPassword,
                encryptedVaultEntry.notes ?: ""
            )
            return UseCaseResult.Success(decryptedVaultEntry)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.ENTRY_DECRYPTION_FAILURE,
                "Vault Guardian",
                e.message ?: "Unknown error"
            )
        }
    }
}