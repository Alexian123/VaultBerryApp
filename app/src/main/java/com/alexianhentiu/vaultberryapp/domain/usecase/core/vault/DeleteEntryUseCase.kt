package com.alexianhentiu.vaultberryapp.domain.usecase.core.vault

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class DeleteEntryUseCase(private val vaultEntryRepository: VaultEntryRepository) {

    suspend operator fun invoke(entry: DecryptedVaultEntry): ActionResult<MessageResponse> {
        return when (val response = vaultEntryRepository.deleteEntry(entry)) {
            is APIResult.Success -> {
                ActionResult.Success(response.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(response.message)
            }
        }
    }
}