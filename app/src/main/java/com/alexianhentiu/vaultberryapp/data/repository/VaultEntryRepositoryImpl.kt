package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.model.VaultEntryDTO
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class VaultEntryRepositoryImpl(
    private val apiService: APIService,
    private val apiResponseHandler: APIResponseHandler
) : VaultEntryRepository {

    override suspend fun getEntries(): APIResult<List<VaultEntry>> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.getEntries() },
            transform = { it?.map { entryDTO -> entryDTO.toDomainModel() } ?: emptyList() }
        )
    }

    override suspend fun addEntry(vaultEntry: VaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.addEntry(vaultEntry.toDBModel()) },
            transform = { }
        )
    }

    override suspend fun modifyEntry(vaultEntry: VaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.modifyEntry(vaultEntry.toDBModel()) },
            transform = { }
        )
    }

    override suspend fun removeEntry(vaultEntry: VaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.removeEntry(vaultEntry.toDBModel()) },
            transform = { }
        )
    }

    private fun VaultEntry.toDBModel() : VaultEntryDTO {
        return VaultEntryDTO(title, url, encryptedUsername, encryptedPassword, notes)
    }

    private fun VaultEntryDTO.toDomainModel() : VaultEntry {
        return VaultEntry(title, url, encryptedUsername, encryptedPassword, notes)
    }
}