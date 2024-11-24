package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.NewEncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class AddEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(newEncryptedVaultEntry: NewEncryptedVaultEntry): APIResult<EncryptedVaultEntry> {
        return vaultEntryRepository.addEntry(newEncryptedVaultEntry)
    }
}