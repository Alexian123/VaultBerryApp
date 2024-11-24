package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class GetEntriesUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(): APIResult<List<EncryptedVaultEntry>> {
        return vaultEntryRepository.getEntries()
    }
}