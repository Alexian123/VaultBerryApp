package com.alexianhentiu.vaultberryapp.domain.usecase.core.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptVaultEntryUseCase

class GetEntriesUseCase(
    private val vaultEntryRepository: VaultEntryRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase
) {
    suspend operator fun invoke(key: DecryptedKey): APIResult<List<DecryptedVaultEntry>> {
        when (val result = vaultEntryRepository.getEntries()) {
            is APIResult.Success -> {
                val encryptedVaultEntries = result.data
                val decryptedVaultEntries = mutableListOf<DecryptedVaultEntry>()
                for (entry in encryptedVaultEntries) {
                    decryptedVaultEntries.add(decryptVaultEntryUseCase(entry, key))
                }
                return APIResult.Success(decryptedVaultEntries)
            }

            is APIResult.Error -> {
                return APIResult.Error(result.message)
            }
        }
    }
}