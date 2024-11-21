package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry

interface VaultEntryRepository {

    suspend fun getEntries(): List<VaultEntry>?

    suspend fun addEntry(vaultEntry: VaultEntry): Boolean

    suspend fun modifyEntry(vaultEntry: VaultEntry): Boolean

    suspend fun removeEntry(vaultEntry: VaultEntry): Boolean
}