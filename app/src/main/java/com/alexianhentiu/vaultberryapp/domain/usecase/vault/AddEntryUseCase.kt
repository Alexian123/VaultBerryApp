package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview

interface AddEntryUseCase {
    suspend operator fun invoke(
        entry: DecryptedVaultEntry,
        key: ByteArray
    ): UseCaseResult<VaultEntryPreview>
}