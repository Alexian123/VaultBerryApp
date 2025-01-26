package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class VaultEntryRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler,
    private val modelConverter: ModelConverter
) : VaultEntryRepository {

    override suspend fun getEntries(): APIResult<List<EncryptedVaultEntry>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getEntries() },
            transform = {
                it?.map {
                    entryDTO -> modelConverter.encryptedVaultEntryFromDTO(entryDTO)
                } ?: emptyList()
            }
        )
    }

    override suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<String> {
        val encryptedVaultEntryDTO = modelConverter.encryptedVaultEntryToDTO(encryptedVaultEntry)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.addEntry(encryptedVaultEntryDTO) },
            transform = { it.message }
        )
    }

    override suspend fun updateEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<String> {
        val encryptedVaultEntryDTO = modelConverter.encryptedVaultEntryToDTO(encryptedVaultEntry)
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateEntry(encryptedVaultEntryDTO) },
            transform = { it.message }
        )
    }

    override suspend fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry): APIResult<String> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteEntry(decryptedVaultEntry.timestamp) },
            transform = { it.message }
        )
    }
}