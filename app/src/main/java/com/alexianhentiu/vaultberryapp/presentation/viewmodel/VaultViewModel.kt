package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.ModifyEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.RemoveEntryUseCase
import com.alexianhentiu.vaultberryapp.presentation.ui.state.VaultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val getEntriesUseCase: GetEntriesUseCase,
    private val addEntryUseCase: AddEntryUseCase,
    private val removeEntryUseCase: RemoveEntryUseCase,
    private val modifyEntryUseCase: ModifyEntryUseCase,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase,
): ViewModel() {

    private val _vaultState = MutableStateFlow<VaultState>(VaultState.Locked)
    val vaultState: StateFlow<VaultState> = _vaultState

    private val _decryptedEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    val decryptedEntries: StateFlow<List<DecryptedVaultEntry>> = _decryptedEntries

    fun getEntries(decryptedVaultKey: DecryptedVaultKey) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = getEntriesUseCase()) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _decryptedEntries.value = decryptAllEntries(result.data, decryptedVaultKey)
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    fun addEntry(
        decryptedVaultKey: DecryptedVaultKey,
        decryptedVaultEntry: DecryptedVaultEntry
    ) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            val newEncryptedVaultEntry =
                encryptVaultEntryUseCase(decryptedVaultEntry, decryptedVaultKey)
            when (val result = addEntryUseCase(newEncryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _decryptedEntries.update { currentList ->
                        currentList + decryptedVaultEntry
                    }
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    fun removeEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = removeEntryUseCase(decryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _decryptedEntries.update { currentList ->
                        currentList.filter { it.timestamp != decryptedVaultEntry.timestamp }
                    }
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    fun modifyEntry(
        decryptedVaultKey: DecryptedVaultKey,
        decryptedVaultEntry: DecryptedVaultEntry
    ) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            val encryptedVaultEntry =
                encryptVaultEntryUseCase(decryptedVaultEntry, decryptedVaultKey)
            when (val result = modifyEntryUseCase(encryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _decryptedEntries.update { currentList ->
                        currentList.map {
                            if (it.timestamp == decryptedVaultEntry.timestamp)
                                decryptedVaultEntry
                            else it
                        }
                    }
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    private fun decryptAllEntries(
        encryptedEntries: List<EncryptedVaultEntry>,
        decryptedVaultKey: DecryptedVaultKey
    ): List<DecryptedVaultEntry> {
        val decryptedEntries = mutableListOf<DecryptedVaultEntry>()
        for (entry in encryptedEntries) {
            val decryptedEntry = decryptVaultEntryUseCase(entry, decryptedVaultKey)
            decryptedEntries.add(decryptedEntry)
        }
        return decryptedEntries
    }

    private fun validateEntry(decryptedEntry: DecryptedVaultEntry): Boolean {
        if (decryptedEntry.title.isBlank()) {
            _vaultState.value = VaultState.Error("Title cannot be empty")
            return false
        }
        if (decryptedEntry.username.isBlank()) {
            _vaultState.value = VaultState.Error("Username cannot be empty")
            return false
        }
        if (decryptedEntry.password.isBlank()) {
            _vaultState.value = VaultState.Error("Password cannot be empty")
            return false
        }
        return true
    }
}