package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class AddEntryUseCase(
    private val vaultRepository: VaultRepository,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        entry: DecryptedVaultEntry,
        key: DecryptedKey
    ): UseCaseResult<VaultEntryPreview> {
        val encryptEntryResult = encryptVaultEntryUseCase(entry, key)
        if (encryptEntryResult is UseCaseResult.Error) {
            return encryptEntryResult
        }
        val newEncryptedVaultEntry = (encryptEntryResult as UseCaseResult.Success).data

        return when (val response = vaultRepository.addEntry(newEncryptedVaultEntry)) {
            is APIResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    response.source,
                    response.message
                )
            }
        }
    }
}