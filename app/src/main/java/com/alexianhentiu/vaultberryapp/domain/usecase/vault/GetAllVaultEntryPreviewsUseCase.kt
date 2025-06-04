package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview

interface GetAllVaultEntryPreviewsUseCase {
    suspend operator fun invoke(): UseCaseResult<List<VaultEntryPreview>>
}