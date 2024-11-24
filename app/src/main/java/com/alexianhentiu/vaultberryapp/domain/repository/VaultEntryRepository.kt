package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.NewEncryptedVaultEntry

interface VaultEntryRepository {

    suspend fun getEntries(): APIResult<List<EncryptedVaultEntry>>

    suspend fun addEntry(newEncryptedVaultEntry: NewEncryptedVaultEntry): APIResult<EncryptedVaultEntry>

    suspend fun modifyEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit>

    suspend fun removeEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit>
}