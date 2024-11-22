package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class AddEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(vaultEntry: VaultEntry): APIResult<Unit> {
        return vaultEntryRepository.addEntry(vaultEntry)
    }
}