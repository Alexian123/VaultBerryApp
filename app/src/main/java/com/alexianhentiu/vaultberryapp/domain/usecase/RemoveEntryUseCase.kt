package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class RemoveEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(vaultEntry: VaultEntry): Boolean {
        return vaultEntryRepository.removeEntry(vaultEntry)
    }
}