package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class GetDecryptedVaultEntryUseCase(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        id: Long,
        decryptedKey: DecryptedKey
    ): UseCaseResult<DecryptedVaultEntry> {
        val vaultEntry = vaultRepository.getVaultEntryDetails(id)
        return if (vaultEntry is APIResult.Success) {
            decryptVaultEntryUseCase(vaultEntry.data, decryptedKey)
        } else {
            val result = (vaultEntry as APIResult.Error)
            UseCaseResult.Error(
                ErrorType.EXTERNAL,
                result.source,
                result.message
            )
        }
    }
}