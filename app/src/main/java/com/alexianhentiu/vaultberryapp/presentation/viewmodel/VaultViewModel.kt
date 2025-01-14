package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
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
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateEntryUseCase: UpdateEntryUseCase,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
    private val encryptVaultEntryUseCase: EncryptVaultEntryUseCase,
    val inputValidator: InputValidator
): ViewModel() {

    private val _vaultState = MutableStateFlow<VaultState>(VaultState.Locked)
    val vaultState: StateFlow<VaultState> = _vaultState

    private val allEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    private val _filteredEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    val filteredEntries: StateFlow<List<DecryptedVaultEntry>> = _filteredEntries

    private lateinit var vaultKey: DecryptedVaultKey

    fun unlockVault(decryptedVaultKey: DecryptedVaultKey?) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            if (decryptedVaultKey == null) {
                Log.e("VaultViewModel", "decryptedVaultKey is null")
                _vaultState.value = VaultState.Error("Invalid vault key")
                return@launch
            }
            vaultKey = decryptedVaultKey
            _vaultState.value = VaultState.Unlocked
        }
    }

    fun searchEntriesByTitle(query: String) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            if (query.isEmpty()) {
                _filteredEntries.value = allEntries.value
            }
            else {
                _filteredEntries.value = allEntries.value.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }
            _vaultState.value = VaultState.Ready
        }
    }

    fun getEntries() {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = getEntriesUseCase()) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Ready
                    allEntries.value = decryptAllEntries(result.data, vaultKey)
                    resetShownEntries()
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    fun addEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            val newEncryptedVaultEntry =
                encryptVaultEntryUseCase(decryptedVaultEntry, vaultKey)
            when (val result = addEntryUseCase(newEncryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Ready
                    allEntries.update { currentList ->
                        currentList + decryptedVaultEntry
                    }
                    resetShownEntries()
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = deleteEntryUseCase(decryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Ready
                    allEntries.update { currentList ->
                        currentList.filter { it.timestamp != decryptedVaultEntry.timestamp }
                    }
                    resetShownEntries()
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                }
            }
        }
    }

    fun updateEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            val encryptedVaultEntry =
                encryptVaultEntryUseCase(decryptedVaultEntry, vaultKey)
            when (val result = updateEntryUseCase(encryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Ready
                    allEntries.update { currentList ->
                        currentList.map {
                            if (it.timestamp == decryptedVaultEntry.timestamp)
                                decryptedVaultEntry
                            else it
                        }
                    }
                    resetShownEntries()
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

    private fun resetShownEntries() {
        _filteredEntries.value = allEntries.value
    }
}