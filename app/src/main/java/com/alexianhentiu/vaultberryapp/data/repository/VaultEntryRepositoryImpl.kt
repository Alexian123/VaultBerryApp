package com.alexianhentiu.vaultberryapp.data.repository

import com.alexianhentiu.vaultberryapp.data.api.APIService
import com.alexianhentiu.vaultberryapp.data.model.VaultEntryDTO
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository

class VaultEntryRepositoryImpl(private val apiService: APIService) : VaultEntryRepository {

    override suspend fun getEntries(): List<VaultEntry>? {
        val response = apiService.getEntries()
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomainModel() }
        } else {
            null
        }
    }

    override suspend fun addEntry(vaultEntry: VaultEntry): Boolean {
        return apiService.addEntry(vaultEntry.toDBModel()).isSuccessful
    }

    override suspend fun modifyEntry(vaultEntry: VaultEntry): Boolean {
        return apiService.modifyEntry(vaultEntry.toDBModel()).isSuccessful
    }

    override suspend fun removeEntry(vaultEntry: VaultEntry): Boolean {
        return apiService.removeEntry(vaultEntry.toDBModel()).isSuccessful
    }

    private fun VaultEntry.toDBModel() : VaultEntryDTO {
        return VaultEntryDTO(title, url, encryptedUsername, encryptedPassword, notes)
    }

    private fun VaultEntryDTO.toDomainModel() : VaultEntry {
        return VaultEntry(title, url, encryptedUsername, encryptedPassword, notes)
    }
}