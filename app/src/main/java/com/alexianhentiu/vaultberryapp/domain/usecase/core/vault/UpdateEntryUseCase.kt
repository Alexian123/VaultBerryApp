package com.alexianhentiu.vaultberryapp.domain.usecase.core.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.EncryptVaultEntryUseCase

class UpdateEntryUseCase(
    private val vaultEntryRepository: VaultEntryRepository,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        entry: DecryptedVaultEntry,
        key: DecryptedKey
    ): APIResult<String> {
        val newEncryptedVaultEntry = encryptVaultEntryUseCase(entry, key)
        return vaultEntryRepository.updateEntry(newEncryptedVaultEntry)
    }
}