package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface VaultRepository {

    suspend fun getAllVaultEntryPreviews(): APIResult<List<VaultEntryPreview>>

    suspend fun getAllVaultEntryDetails(): APIResult<List<EncryptedVaultEntry>>

    suspend fun getVaultEntryDetails(id: Long): APIResult<EncryptedVaultEntry>

    suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<VaultEntryPreview>

    suspend fun updateEntry(
        id: Long,
        encryptedVaultEntry: EncryptedVaultEntry
    ): APIResult<MessageResponse>

    suspend fun deleteEntry(id: Long): APIResult<MessageResponse>

    suspend fun searchVaultEntries(keywords: List<String>): APIResult<List<EncryptedVaultEntry>>
}