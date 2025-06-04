package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface UpdateEntryUseCase {
    suspend operator fun invoke(
        id: Long,
        entry: DecryptedVaultEntry,
        key: ByteArray
    ): UseCaseResult<MessageResponse>
}