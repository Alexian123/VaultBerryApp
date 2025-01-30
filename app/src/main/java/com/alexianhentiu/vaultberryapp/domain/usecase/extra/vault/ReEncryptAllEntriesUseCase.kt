package com.alexianhentiu.vaultberryapp.domain.usecase.extra.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.EncryptVaultEntryUseCase

class ReEncryptAllEntriesUseCase(
    private val vaultEntryRepository: VaultEntryRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        oldKey: DecryptedKey,
        newKey: DecryptedKey
    ): APIResult<String> {
        when (val result = vaultEntryRepository.getEntries()) {
            is APIResult.Success -> {
                val encryptedVaultEntries = result.data
                for (entry in encryptedVaultEntries) {
                    val decryptedEntry = decryptVaultEntryUseCase(entry, oldKey)
                    val newEncryptedEntry = encryptVaultEntryUseCase(decryptedEntry, newKey)

                    when (val updateResult = vaultEntryRepository.updateEntry(newEncryptedEntry)) {
                        is APIResult.Success -> {
                            // Entry updated successfully
                        }

                        is APIResult.Error -> {
                            return APIResult.Error(updateResult.message)
                        }
                    }
                }

                return APIResult.Success("All entries re-encrypted successfully")
            }

            is APIResult.Error -> {
                return APIResult.Error(result.message)
            }
        }
    }
}