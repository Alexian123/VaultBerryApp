package com.alexianhentiu.vaultberryapp.domain.repository

import com.alexianhentiu.vaultberryapp.domain.model.VaultEntry

interface VaultEntryRepository {

    suspend fun getEntries(): List<VaultEntry>?

    suspend fun addEntry(vaultEntry: VaultEntry)

    suspend fun removeEntry(vaultEntry: VaultEntry)
}