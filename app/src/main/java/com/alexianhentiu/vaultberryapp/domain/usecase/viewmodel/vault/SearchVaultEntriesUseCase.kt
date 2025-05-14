package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class SearchVaultEntriesUseCase(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        keywords: List<String>,
        decryptedKey: ByteArray
    ): UseCaseResult<List<DecryptedVaultEntry>> {
        val vaultEntriesResult = vaultRepository.searchVaultEntries(keywords)
        if (vaultEntriesResult is APIResult.Error) {
            return UseCaseResult.Error(
                ErrorType.EXTERNAL,
                vaultEntriesResult.source,
                vaultEntriesResult.message
            )
        }
        val vaultEntries = (vaultEntriesResult as APIResult.Success).data
        val decryptedVaultEntries = vaultEntries.map { vaultEntry ->
            when (val decryptResult = decryptVaultEntryUseCase(vaultEntry, decryptedKey)) {
                is UseCaseResult.Success -> decryptResult.data
                is UseCaseResult.Error -> return decryptResult
            }
        }
        return UseCaseResult.Success(decryptedVaultEntries)
    }
}