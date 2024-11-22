package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry

interface VaultEntryRepository {

    suspend fun getEntries(): APIResult<List<VaultEntry>>

    suspend fun addEntry(vaultEntry: VaultEntry): APIResult<Unit>

    suspend fun modifyEntry(vaultEntry: VaultEntry): APIResult<Unit>

    suspend fun removeEntry(vaultEntry: VaultEntry): APIResult<Unit>
}