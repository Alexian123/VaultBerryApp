package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface VaultRepository {

    suspend fun getAllVaultEntryPreviews(): ApiResult<List<VaultEntryPreview>>

    suspend fun getAllVaultEntryDetails(): ApiResult<List<EncryptedVaultEntry>>

    suspend fun getVaultEntryDetails(id: Long): ApiResult<EncryptedVaultEntry>

    suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): ApiResult<VaultEntryPreview>

    suspend fun updateEntry(
        id: Long,
        encryptedVaultEntry: EncryptedVaultEntry
    ): ApiResult<MessageResponse>

    suspend fun deleteEntry(id: Long): ApiResult<MessageResponse>

    suspend fun searchVaultEntries(keywords: List<String>): ApiResult<List<EncryptedVaultEntry>>
}