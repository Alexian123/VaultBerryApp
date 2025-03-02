package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.VaultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getEntriesUseCase: GetEntriesUseCase,
    private val addEntryUseCase: AddEntryUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateEntryUseCase: UpdateEntryUseCase,
    val inputValidator: InputValidator
): ViewModel() {

    private val _vaultState = MutableStateFlow<VaultState>(VaultState.Locked)
    val vaultState: StateFlow<VaultState> = _vaultState

    private val allEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    private val _filteredEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    val filteredEntries: StateFlow<List<DecryptedVaultEntry>> = _filteredEntries

    private lateinit var vaultKey: DecryptedKey

    // TODO: Implement statistics & suggestions for entry password security

    fun logout() {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = logoutUseCase()) {
                is APIResult.Success -> {
                    allEntries.value = emptyList()
                    _filteredEntries.value = emptyList()
                    vaultKey = DecryptedKey(ByteArray(0))
                    _vaultState.value = VaultState.Locked
                    Log.d("VaultViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                    Log.e("VaultViewModel", "Logout failed: ${result.message}")
                }
            }
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
            _vaultState.value = VaultState.Unlocked
        }
    }

    fun getEntries(decryptedKey: DecryptedKey?) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            if (decryptedKey == null) {
                Log.e("VaultViewModel", "decryptedVaultKey is null")
                _vaultState.value = VaultState.Error("Invalid vault key")
                return@launch
            }
            vaultKey = decryptedKey
            when (val result = getEntriesUseCase(vaultKey)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.value = result.data
                    resetShownEntries()
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                    Log.e("VaultViewModel", "Failed to get entries: ${result.message}")
                }
            }
        }
    }

    fun addEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = addEntryUseCase(decryptedVaultEntry, vaultKey)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.update { currentList ->
                        currentList + decryptedVaultEntry
                    }
                    resetShownEntries()
                    Log.d("VaultViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                    Log.e("VaultViewModel", "Failed to add entry: ${result.message}")
                }
            }
        }
    }

    fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = deleteEntryUseCase(decryptedVaultEntry)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.update { currentList ->
                        currentList.filter { it.timestamp != decryptedVaultEntry.timestamp }
                    }
                    resetShownEntries()
                    Log.d("VaultViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                    Log.e("VaultViewModel", "Failed to delete entry: ${result.message}")
                }
            }
        }
    }

    fun updateEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = updateEntryUseCase(decryptedVaultEntry, vaultKey)) {
                is APIResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.update { currentList ->
                        currentList.map {
                            if (it.timestamp == decryptedVaultEntry.timestamp)
                                decryptedVaultEntry
                            else it
                        }
                    }
                    resetShownEntries()
                    Log.d("VaultViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _vaultState.value = VaultState.Error(result.message)
                    Log.e("VaultViewModel", "Failed to update entry: ${result.message}")
                }
            }
        }
    }

    fun resetState() {
        _vaultState.value = VaultState.Locked
    }

    private fun resetShownEntries() {
        _filteredEntries.value = allEntries.value
    }
}