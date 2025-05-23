package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class DecryptVaultEntryUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        encryptedVaultEntry: EncryptedVaultEntry,
        decryptedKey: ByteArray
    ): UseCaseResult<DecryptedVaultEntry> {
        try {
            var decryptedUsername = ""
            if (encryptedVaultEntry.encryptedUsername != null) {
                decryptedUsername = vaultGuardian.decryptField(
                    encryptedVaultEntry.encryptedUsername,
                    decryptedKey
                )
            }

            var decryptedPassword = ""
            if (encryptedVaultEntry.encryptedPassword != null) {
                decryptedPassword = vaultGuardian.decryptField(
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
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Entry decryption failed: ${e.message}"
            )
        }
    }
}