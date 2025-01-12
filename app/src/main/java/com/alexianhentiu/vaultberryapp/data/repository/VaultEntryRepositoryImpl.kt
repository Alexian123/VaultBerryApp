package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class VaultEntryRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler
) : VaultEntryRepository {

    override suspend fun getEntries(): APIResult<List<EncryptedVaultEntry>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getEntries() },
            transform = { it?.map { entryDTO -> entryDTO.toDomainModel() } ?: emptyList() }
        )
    }

    override suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.addEntry(encryptedVaultEntry.toDBModel()) },
            transform = { }
        )
    }

    override suspend fun updateEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.updateEntry(encryptedVaultEntry.toDBModel()) },
            transform = { }
        )
    }

    override suspend fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.deleteEntry(decryptedVaultEntry.timestamp) },
            transform = { }
        )
    }

    private fun EncryptedVaultEntry.toDBModel() : EncryptedVaultEntryDTO {
        return EncryptedVaultEntryDTO(timestamp, title, url, encryptedUsername, encryptedPassword, notes)
    }

    private fun EncryptedVaultEntryDTO.toDomainModel() : EncryptedVaultEntry {
        return EncryptedVaultEntry(timestamp, title, url, encryptedUsername, encryptedPassword, notes)
    }
}