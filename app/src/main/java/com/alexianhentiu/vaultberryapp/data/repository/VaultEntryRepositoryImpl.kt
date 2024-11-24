package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.model.EncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.data.model.NewEncryptedVaultEntryDTO
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.NewEncryptedVaultEntry
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

    override suspend fun addEntry(newEncryptedVaultEntry: NewEncryptedVaultEntry): APIResult<EncryptedVaultEntry> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.addEntry(newEncryptedVaultEntry.toDBModel()) },
            transform = { it.toDomainModel() }
        )
    }

    override suspend fun modifyEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.modifyEntry(encryptedVaultEntry.toDBModel()) },
            transform = { }
        )
    }

    override suspend fun removeEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit> {
        return apiResponseHandler.safeApiCall(
            apiCall = { apiService.removeEntry(encryptedVaultEntry.toDBModel()) },
            transform = { }
        )
    }

    private fun EncryptedVaultEntry.toDBModel() : EncryptedVaultEntryDTO {
        return EncryptedVaultEntryDTO(id, title, url, encryptedUsername, encryptedPassword, notes)
    }

    private fun EncryptedVaultEntryDTO.toDomainModel() : EncryptedVaultEntry {
        return EncryptedVaultEntry(id, title, url, encryptedUsername, encryptedPassword, notes)
    }

    private fun NewEncryptedVaultEntry.toDBModel() : NewEncryptedVaultEntryDTO {
        return NewEncryptedVaultEntryDTO(title, url, encryptedUsername, encryptedPassword, notes)
    }

}