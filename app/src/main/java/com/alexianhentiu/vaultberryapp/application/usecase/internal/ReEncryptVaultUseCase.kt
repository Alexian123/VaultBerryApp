package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import javax.inject.Inject

class ReEncryptVaultUseCase @Inject constructor(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        oldKey: ByteArray,
        newKey: ByteArray
    ): UseCaseResult<MessageResponse> {
        when (val previewsResult = vaultRepository.getAllVaultEntryPreviews()) {
            is ApiResult.Success -> {
                return when (val detailsResult = vaultRepository.getAllVaultEntryDetails()) {
                    is ApiResult.Success -> {
                        reEncryptEntries(previewsResult.data, detailsResult.data, oldKey, newKey)
                    }

                    is ApiResult.Error -> {
                        return UseCaseResult.Error(
                            ErrorInfo(
                                ErrorType.API,
                                detailsResult.source,
                                detailsResult.message
                            )
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        previewsResult.source,
                        previewsResult.message
                    )
                )
            }
        }
    }

    private suspend fun reEncryptEntries(
        previews: List<VaultEntryPreview>,
        details: List<EncryptedVaultEntry>,
        oldKey: ByteArray,
        newKey: ByteArray
    ): UseCaseResult<MessageResponse> {
        for (entry in details) {
            val decryptedEntryResult = decryptVaultEntryUseCase(entry, oldKey)
            if (decryptedEntryResult is UseCaseResult.Error) {
                return decryptedEntryResult
            }
            val decryptedEntry = (decryptedEntryResult as UseCaseResult.Success).data

            val encryptedEntryResult = encryptVaultEntryUseCase(decryptedEntry, newKey)
            if (encryptedEntryResult is UseCaseResult.Error) {
                return encryptedEntryResult
            }
            val newEncryptedEntry = (encryptedEntryResult as UseCaseResult.Success).data

            val id = previews.find { it.title == entry.title }?.id ?: continue
            val updateResult = vaultRepository.updateEntry(id, newEncryptedEntry)
            if (updateResult is ApiResult.Error) {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        updateResult.source,
                        updateResult.message
                    )
                )
            }
        }
        return UseCaseResult.Success(MessageResponse("Success"))
    }
}