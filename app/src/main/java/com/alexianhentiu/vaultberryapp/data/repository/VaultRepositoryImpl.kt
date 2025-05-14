package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository

class VaultRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler,
    private val modelConverter: ModelConverter
) : VaultRepository {

    override suspend fun getAllVaultEntryPreviews(): APIResult<List<VaultEntryPreview>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAllVaultEntryPreviews() },
            transform = {
                it?.map {
                    entryDTO -> modelConverter.vaultEntryPreviewFromDTO(entryDTO)
                } ?: emptyList()
            }
        )
    }

    override suspend fun getAllVaultEntryDetails(): APIResult<List<EncryptedVaultEntry>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getAllVaultEntryDetails() },
            transform = {
                it?.map {
                    entryDTO -> modelConverter.encryptedVaultEntryFromDTO(entryDTO)
                } ?: emptyList()
            }
        )
    }

    override suspend fun getVaultEntryDetails(id: Long): APIResult<EncryptedVaultEntry> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getVaultEntryDetails(id) },
            transform = { modelConverter.encryptedVaultEntryFromDTO(it) }
        )
    }

    override suspend fun addEntry(
        encryptedVaultEntry: EncryptedVaultEntry
    ): APIResult<VaultEntryPreview> {
        val encryptedVaultEntryDTO = modelConverter.encryptedVaultEntryToDTO(encryptedVaultEntry)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.addEntry(encryptedVaultEntryDTO) },
            transform = { modelConverter.vaultEntryPreviewFromDTO(it) }
        )
    }

    override suspend fun updateEntry(
        id: Long,
        encryptedVaultEntry: EncryptedVaultEntry
    ): APIResult<MessageResponse> {
        val encryptedVaultEntryDTO = modelConverter.encryptedVaultEntryToDTO(encryptedVaultEntry)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateEntry(id, encryptedVaultEntryDTO) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun deleteEntry(id: Long): APIResult<MessageResponse> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteEntry(id) },
            transform = { modelConverter.messageResponseFromDTO(it) }
        )
    }

    override suspend fun searchVaultEntries(
        keywords: List<String>
    ): APIResult<List<EncryptedVaultEntry>> {
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