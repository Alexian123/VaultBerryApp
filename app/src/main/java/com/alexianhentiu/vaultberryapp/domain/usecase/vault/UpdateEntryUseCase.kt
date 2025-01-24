package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class UpdateEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit> {
        return vaultEntryRepository.updateEntry(encryptedVaultEntry)
    }

}