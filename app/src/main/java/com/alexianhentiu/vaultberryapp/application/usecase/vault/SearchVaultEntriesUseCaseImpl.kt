package com.alexianhentiu.vaultberryapp.application.usecase.vault

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.SearchVaultEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class SearchVaultEntriesUseCaseImpl(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val stringResourceProvider: StringResourceProvider
) : SearchVaultEntriesUseCase {
    override suspend operator fun invoke(
        keywords: List<String>,
        decryptedKey: ByteArray
    ): UseCaseResult<List<DecryptedVaultEntry>> {
        try {
            val vaultEntriesResult = vaultRepository.searchVaultEntries(keywords)
            if (vaultEntriesResult is ApiResult.Error) {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        vaultEntriesResult.source,
                        vaultEntriesResult.message
                    )
                )
            }
            val vaultEntries = (vaultEntriesResult as ApiResult.Success).data
            val decryptedVaultEntries = vaultEntries.map { vaultEntry ->
                when (val decryptResult = decryptVaultEntryUseCase(vaultEntry, decryptedKey)) {
                    is UseCaseResult.Success -> decryptResult.data
                    is UseCaseResult.Error -> return decryptResult
                }
            }
            return UseCaseResult.Success(decryptedVaultEntries)
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