package com.alexianhentiu.vaultberryapp.application.usecase.vault

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetAllVaultEntryPreviewsUseCase

class GetAllVaultEntryPreviewsUseCaseImpl(
    private val vaultRepository: VaultRepository,
) : GetAllVaultEntryPreviewsUseCase {
    override suspend operator fun invoke(): UseCaseResult<List<VaultEntryPreview>> {
        return when (val result = vaultRepository.getAllVaultEntryPreviews()) {
            is ApiResult.Success -> {
                UseCaseResult.Success(result.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        result.source,
                        result.message
                    )
                )
            }
        }
    }
}