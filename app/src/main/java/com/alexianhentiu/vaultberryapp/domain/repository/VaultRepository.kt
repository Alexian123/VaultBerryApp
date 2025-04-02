package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface VaultRepository {

    suspend fun getEntries(): APIResult<List<EncryptedVaultEntry>>

    suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<MessageResponse>

    suspend fun updateEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<MessageResponse>

    suspend fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry): APIResult<MessageResponse>
}