package com.alexianhentiu.vaultberryapp.domain.usecase.core.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GetEntriesUseCase(
    private val vaultEntryRepository: VaultEntryRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase
) {
    suspend operator fun invoke(key: DecryptedKey): ActionResult<List<DecryptedVaultEntry>> {
        when (val result = vaultEntryRepository.getEntries()) {
            is APIResult.Success -> {
                val encryptedVaultEntries = result.data
                val decryptedVaultEntries = mutableListOf<DecryptedVaultEntry>()
                for (entry in encryptedVaultEntries) {
                    val decryptEntryResult = decryptVaultEntryUseCase(entry, key)
                    if (decryptEntryResult is ActionResult.Error) {
                        return decryptEntryResult
                    }
                    decryptedVaultEntries.add((decryptEntryResult as ActionResult.Success).data)
                }
                return ActionResult.Success(decryptedVaultEntries)
            }

            is APIResult.Error -> {
                return ActionResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}