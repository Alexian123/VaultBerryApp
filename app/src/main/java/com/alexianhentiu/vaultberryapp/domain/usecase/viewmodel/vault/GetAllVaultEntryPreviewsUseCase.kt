package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class GetAllVaultEntryPreviewsUseCase(
    private val vaultRepository: VaultRepository,
) {
    suspend operator fun invoke(): UseCaseResult<List<VaultEntryPreview>> {
        return when (val result = vaultRepository.getAllVaultEntryPreviews()) {
            is APIResult.Success -> {
                UseCaseResult.Success(result.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}