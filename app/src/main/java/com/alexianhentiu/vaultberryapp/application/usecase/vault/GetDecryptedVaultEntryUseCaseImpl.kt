package com.alexianhentiu.vaultberryapp.application.usecase.vault

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetDecryptedVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class GetDecryptedVaultEntryUseCaseImpl(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val stringResourceProvider: StringResourceProvider
) : GetDecryptedVaultEntryUseCase {
    override suspend operator fun invoke(
        id: Long,
        decryptedKey: ByteArray
    ): UseCaseResult<DecryptedVaultEntry> {
        try {
            val vaultEntry = vaultRepository.getVaultEntryDetails(id)
            return if (vaultEntry is ApiResult.Success) {
                decryptVaultEntryUseCase(vaultEntry.data, decryptedKey)
            } else {
                val result = (vaultEntry as ApiResult.Error)
                UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        result.source,
                        result.message
                    )
                )
            }
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}