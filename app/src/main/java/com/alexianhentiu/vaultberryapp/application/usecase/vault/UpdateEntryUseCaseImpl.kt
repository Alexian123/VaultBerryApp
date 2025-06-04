package com.alexianhentiu.vaultberryapp.application.usecase.vault

import com.alexianhentiu.vaultberryapp.application.usecase.internal.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase

class UpdateEntryUseCaseImpl(
    private val vaultRepository: VaultRepository,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) : UpdateEntryUseCase {
    override suspend operator fun invoke(
        id: Long,
        entry: DecryptedVaultEntry,
        key: ByteArray
    ): UseCaseResult<MessageResponse> {
        val encryptEntryResult = encryptVaultEntryUseCase(entry, key)
        if (encryptEntryResult is UseCaseResult.Error) {
            return encryptEntryResult
        }
        val newEncryptedVaultEntry = (encryptEntryResult as UseCaseResult.Success).data

        return when (val response = vaultRepository.updateEntry(id, newEncryptedVaultEntry)) {
            is ApiResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.API,
                    response.source,
                    response.message
                )
            }
        }
    }
}