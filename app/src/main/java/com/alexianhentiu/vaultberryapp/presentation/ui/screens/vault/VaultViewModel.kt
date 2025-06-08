package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetAllVaultEntryPreviewsUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetDecryptedVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val getAllVaultEntryPreviewsUseCase: GetAllVaultEntryPreviewsUseCase,
    private val getDecryptedVaultEntryUseCase: GetDecryptedVaultEntryUseCase,
    private val addEntryUseCase: AddEntryUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateEntryUseCase: UpdateEntryUseCase
): ViewModel() {

    private val _vaultScreenState = MutableStateFlow<VaultScreenState>(VaultScreenState.Locked)
    val vaultScreenState: StateFlow<VaultScreenState> = _vaultScreenState

    private val _allPreviews = MutableStateFlow<List<VaultEntryPreview>>(emptyList())
    private val _filteredPreviews = MutableStateFlow<List<VaultEntryPreview>>(emptyList())
    val filteredPreviews: StateFlow<List<VaultEntryPreview>> = _filteredPreviews

    private val _expandedEntriesMap = MutableStateFlow<Map<Long, DecryptedVaultEntry?>>(emptyMap())
    val expandedEntriesMap: StateFlow<Map<Long, DecryptedVaultEntry?>> = _expandedEntriesMap

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshVaultEntries() {
        if (_isRefreshing.value) {
            return
        }
        viewModelScope.launch {
            _isRefreshing.value = true
            when (val result = getAllVaultEntryPreviewsUseCase()) {
                is UseCaseResult.Success -> {
                    _allPreviews.value = result.data
                    resetShownEntries()
                }

                is UseCaseResult.Error -> {
                    _vaultScreenState.value = VaultScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
            _isRefreshing.value = false
        }
    }

    fun searchPreviewsByTitle(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                resetShownEntries()
            }
            else {
                _filteredPreviews.value = _allPreviews.value.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }
        }
    }

    fun fetchPreviews() {
        viewModelScope.launch {
            _vaultScreenState.value = VaultScreenState.Loading
            when (val result = getAllVaultEntryPreviewsUseCase()) {
                is UseCaseResult.Success -> {
                    _vaultScreenState.value = VaultScreenState.Unlocked
                    _allPreviews.value = result.data
                    resetShownEntries()
                }

                is UseCaseResult.Error -> {
                    _vaultScreenState.value = VaultScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun fetchEntryDetails(id: Long, decryptedKey: ByteArray) {
        viewModelScope.launch {
            when (val result = getDecryptedVaultEntryUseCase(id, decryptedKey)) {
                is UseCaseResult.Success -> {
                    _expandedEntriesMap.update { it + (id to result.data) }
                }
                is UseCaseResult.Error -> {
                    _vaultScreenState.value = VaultScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun clearEntryDetails(id: Long) {
        viewModelScope.launch {
            _vaultScreenState.value = VaultScreenState.Loading
            _expandedEntriesMap.update { it + (id to null) }
            _vaultScreenState.value = VaultScreenState.Unlocked
        }
    }

    fun addEntry(decryptedVaultEntry: DecryptedVaultEntry, decryptedKey: ByteArray) {
        viewModelScope.launch {
            _vaultScreenState.value = VaultScreenState.Loading
            when (val result = addEntryUseCase(decryptedVaultEntry, decryptedKey)) {
                is UseCaseResult.Success -> {
                    _vaultScreenState.value = VaultScreenState.Unlocked
                    _allPreviews.update { currentList ->
                        currentList + result.data
                    }
                    resetShownEntries()
                }

                is UseCaseResult.Error -> {
                    _vaultScreenState.value = VaultScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            _vaultScreenState.value = VaultScreenState.Loading
            when (val result = deleteEntryUseCase(id)) {
                is UseCaseResult.Success -> {
                    _vaultScreenState.value = VaultScreenState.Unlocked
                    _allPreviews.update { currentList ->
                        currentList.filter { it.id != id }
                    }
                    resetShownEntries()
                }

                is UseCaseResult.Error -> {
                    _vaultScreenState.value = VaultScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun updateEntry(id: Long, decryptedVaultEntry: DecryptedVaultEntry, decryptedKey: ByteArray) {
        viewModelScope.launch {
            _vaultScreenState.value = VaultScreenState.Loading
            when (val result = updateEntryUseCase(id, decryptedVaultEntry, decryptedKey)) {
                is UseCaseResult.Success -> {
                    _vaultScreenState.value = VaultScreenState.Unlocked
                    _allPreviews.update { currentList ->
                        currentList.map {
                            if (it.id == id)
                                it.copy(title = decryptedVaultEntry.title)
                            else it
                        }
                    }
                    resetShownEntries()
                }

                is UseCaseResult.Error -> {
                    _vaultScreenState.value = VaultScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun resetState() {
        clearData()
        _vaultScreenState.value = VaultScreenState.Locked
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }

    fun clearData() {
        _allPreviews.value = emptyList()
        _filteredPreviews.value = emptyList()
        _expandedEntriesMap.value = emptyMap()
    }

    private fun resetShownEntries() {
        _filteredPreviews.value = _allPreviews.value
        _expandedEntriesMap.value = _allPreviews.value.associate { it.id to null }
    }
}