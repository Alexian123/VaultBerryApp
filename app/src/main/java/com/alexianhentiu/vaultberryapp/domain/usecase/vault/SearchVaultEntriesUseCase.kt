package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry

interface SearchVaultEntriesUseCase {
    suspend operator fun invoke(
        keywords: List<String>,
        decryptedKey: ByteArray
    ): UseCaseResult<List<DecryptedVaultEntry>>
}