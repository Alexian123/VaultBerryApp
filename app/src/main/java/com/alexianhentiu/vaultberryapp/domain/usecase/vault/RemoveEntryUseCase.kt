package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class RemoveEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(decryptedVaultEntry: DecryptedVaultEntry): APIResult<Unit> {
        return vaultEntryRepository.removeEntry(decryptedVaultEntry)
    }
}