package com.alexianhentiu.vaultberryapp.data.repository.remote

import com.alexianhentiu.vaultberryapp.data.remote.ApiResponseHandler
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.data.remote.ModelConverter
import com.alexianhentiu.vaultberryapp.data.remote.api.ApiService
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository

class RemoteVaultRepository(
    private val apiService: ApiService,
    private val apiResponseHandler: ApiResponseHandler,
    private val modelConverter: ModelConverter
) : VaultRepository {

    override suspend fun getAllVaultEntryPreviews(): ApiResult<List<VaultEntryPreview>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAllVaultEntryPreviews() },
            transform = {
                it?.map {
                    entryDTO -> modelConverter.vaultEntryPreviewFromDTO(entryDTO)
                } ?: emptyList()
            }
        )
    }

    override suspend fun getAllVaultEntryDetails(): ApiResult<List<EncryptedVaultEntry>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAllVaultEntryDetails() },
            transform = {
                it?.map {
                    entryDTO -> modelConverter.encryptedVaultEntryFromDTO(entryDTO)
                } ?: emptyList()
            }
        )
    }

    override suspend fun getVaultEntryDetails(id: Long): ApiResult<EncryptedVaultEntry> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getVaultEntryDetails(id) },
            transform = { modelConverter.encryptedVaultEntryFromDTO(it) }
        )
    }

    override suspend fun addEntry(
        encryptedVaultEntry: EncryptedVaultEntry
    ): ApiResult<VaultEntryPreview> {
        val encryptedVaultEntryDTO = modelConverter.encryptedVaultEntryToDTO(encryptedVaultEntry)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.addEntry(encryptedVaultEntryDTO) },
            transform = { modelConverter.vaultEntryPreviewFromDTO(it) }
        )
    }

    override suspend fun updateEntry(
        id: Long,
        encryptedVaultEntry: EncryptedVaultEntry
    ): ApiResult<MessageResponse> {
        val encryptedVaultEntryDTO = modelConverter.encryptedVaultEntryToDTO(encryptedVaultEntry)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateEntry(id, encryptedVaultEntryDTO) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun deleteEntry(id: Long): ApiResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteEntry(id) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun searchVaultEntries(
        keywords: List<String>
    ): ApiResult<List<EncryptedVaultEntry>> {
        val keywordsDTO = modelConverter.keywordsToVaultSearchRequest(keywords)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.searchVaultEntries(keywordsDTO) },
            transform = {
                it?.map { entryDTO ->
                    modelConverter.encryptedVaultEntryFromDTO(entryDTO)
                } ?: emptyList()
            }
        )
    }
}