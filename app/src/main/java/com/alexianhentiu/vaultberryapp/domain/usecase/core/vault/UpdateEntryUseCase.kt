package com.alexianhentiu.vaultberryapp.domain.usecase.core.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class UpdateEntryUseCase(
    private val vaultEntryRepository: VaultEntryRepository,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        entry: DecryptedVaultEntry,
        key: DecryptedKey
    ): ActionResult<MessageResponse> {
        val encryptEntryResult = encryptVaultEntryUseCase(entry, key)
        if (encryptEntryResult is ActionResult.Error) {
            return encryptEntryResult
        }
        val newEncryptedVaultEntry = (encryptEntryResult as ActionResult.Success).data

        return when (val response = vaultEntryRepository.updateEntry(newEncryptedVaultEntry)) {
            is APIResult.Success -> {
                ActionResult.Success(response.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(response.message)
            }
        }
    }
}