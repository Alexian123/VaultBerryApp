package com.alexianhentiu.vaultberryapp.application.usecase.vault

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class UpdateEntryUseCaseImpl(
    private val vaultRepository: VaultRepository,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase,
    private val stringResourceProvider: StringResourceProvider
) : UpdateEntryUseCase {
    override suspend operator fun invoke(
        id: Long,
        entry: DecryptedVaultEntry,
        key: ByteArray
    ): UseCaseResult<MessageResponse> {
        try {
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
                        ErrorInfo(
                            ErrorType.API,
                            response.source,
                            response.message
                        )
                    )
                }
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