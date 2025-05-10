package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GetAllVaultEntryPreviewsUseCase(
    private val vaultRepository: VaultRepository,
) {
    suspend operator fun invoke(): ActionResult<List<VaultEntryPreview>> {
        return when (val result = vaultRepository.getAllVaultEntryPreviews()) {
            is APIResult.Success -> {
                ActionResult.Success(result.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}