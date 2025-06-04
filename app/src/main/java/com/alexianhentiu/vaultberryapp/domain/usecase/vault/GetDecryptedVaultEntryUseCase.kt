package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry

interface GetDecryptedVaultEntryUseCase {
    suspend operator fun invoke(
        id: Long,
        decryptedKey: ByteArray
    ): UseCaseResult<DecryptedVaultEntry>
}