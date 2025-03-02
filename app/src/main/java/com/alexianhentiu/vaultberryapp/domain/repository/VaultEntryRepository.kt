package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse

interface VaultEntryRepository {

    suspend fun getEntries(): APIResult<List<EncryptedVaultEntry>>

    suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<MessageResponse>

    suspend fun updateEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<MessageResponse>

    suspend fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry): APIResult<MessageResponse>
}