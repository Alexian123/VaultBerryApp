package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class GetEntriesUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(): APIResult<List<VaultEntry>> {
        return vaultEntryRepository.getEntries()
    }
}