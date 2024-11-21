package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class AddEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(vaultEntry: VaultEntry): Boolean {
        return vaultEntryRepository.addEntry(vaultEntry)
    }
}