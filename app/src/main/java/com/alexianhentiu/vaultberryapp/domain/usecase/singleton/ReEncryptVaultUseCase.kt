package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import android.util.Log
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ReEncryptVaultUseCase(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        oldKey: DecryptedKey,
        newKey: DecryptedKey
    ): UseCaseResult<MessageResponse> {
        when (val previewsResult = vaultRepository.getAllVaultEntryPreviews()) {
            is APIResult.Success -> {
                return when (val detailsResult = vaultRepository.getAllVaultEntryDetails()) {
                    is APIResult.Success -> {
                        reEncryptEntries(previewsResult.data, detailsResult.data, oldKey, newKey)
                    }

                    is APIResult.Error -> {
                        return UseCaseResult.Error(
                            ErrorType.EXTERNAL,
                            detailsResult.source,
                            detailsResult.message
                        )
                    }
                }
            }

            is APIResult.Error -> {
                return UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    previewsResult.source,
                    previewsResult.message
                )
            }
        }
    }

    private suspend fun reEncryptEntries(
        previews: List<VaultEntryPreview>,
        details: List<EncryptedVaultEntry>,
        oldKey: DecryptedKey,
        newKey: DecryptedKey
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
            if (updateResult is APIResult.Error) {
                return UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    updateResult.source,
                    updateResult.message
                )
            }
            val updateMessageResponse = (updateResult as APIResult.Success).data
            Log.d("ReEncryptVaultUseCase", updateMessageResponse.message)
        }
        return UseCaseResult.Success(MessageResponse("All entries re-encrypted successfully"))
    }
}