package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry

interface VaultEntryRepository {

    suspend fun getEntries(): APIResult<List<EncryptedVaultEntry>>

    suspend fun addEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit>

    suspend fun modifyEntry(encryptedVaultEntry: EncryptedVaultEntry): APIResult<Unit>

    suspend fun removeEntry(decryptedVaultEntry: DecryptedVaultEntry): APIResult<Unit>
}