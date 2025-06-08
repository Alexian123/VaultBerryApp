package com.alexianhentiu.vaultberryapp.application.usecase.vault

import com.alexianhentiu.vaultberryapp.application.usecase.internal.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import javax.inject.Inject

class AddEntryUseCaseImpl @Inject constructor(
    private val vaultRepository: VaultRepository,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase
) : AddEntryUseCase {
    override suspend operator fun invoke(
        entry: DecryptedVaultEntry,
        key: ByteArray
    ): UseCaseResult<VaultEntryPreview> {
        val encryptEntryResult = encryptVaultEntryUseCase(entry, key)
        if (encryptEntryResult is UseCaseResult.Error) {
            return encryptEntryResult
        }
        val newEncryptedVaultEntry = (encryptEntryResult as UseCaseResult.Success).data

        return when (val response = vaultRepository.addEntry(newEncryptedVaultEntry)) {
            is ApiResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        response.source,
                        response.message
                    )
                )
            }
        }
    }
}