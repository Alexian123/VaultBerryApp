package com.alexianhentiu.vaultberryapp.domain.usecase.core.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class DeleteEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(entry: DecryptedVaultEntry): APIResult<String> {
        return vaultEntryRepository.deleteEntry(entry)
    }
}