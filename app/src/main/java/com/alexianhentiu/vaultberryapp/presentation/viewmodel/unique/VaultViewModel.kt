package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.GetAllVaultEntryPreviewsUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.GetDecryptedVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.states.VaultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getAllVaultEntryPreviewsUseCase: GetAllVaultEntryPreviewsUseCase,
    private val getDecryptedVaultEntryUseCase: GetDecryptedVaultEntryUseCase,
    private val addEntryUseCase: AddEntryUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateEntryUseCase: UpdateEntryUseCase,
    val inputValidator: InputValidator,
    val passwordEvaluator: PasswordEvaluator
): ViewModel() {

    private val _vaultState = MutableStateFlow<VaultState>(VaultState.Locked)
    val vaultState: StateFlow<VaultState> = _vaultState

    private val _allPreviews = MutableStateFlow<List<VaultEntryPreview>>(emptyList())
    private val _filteredPreviews = MutableStateFlow<List<VaultEntryPreview>>(emptyList())
    val filteredPreviews: StateFlow<List<VaultEntryPreview>> = _filteredPreviews

    private val _expandedEntriesMap = MutableStateFlow<Map<Long, DecryptedVaultEntry?>>(emptyMap())
    val expandedEntriesMap: StateFlow<Map<Long, DecryptedVaultEntry?>> = _expandedEntriesMap

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private lateinit var vaultKey: DecryptedKey

    fun refreshVaultEntries() {
        if (_isRefreshing.value) {
            return
        }
        viewModelScope.launch {
            _isRefreshing.value = true
            when (val result = getAllVaultEntryPreviewsUseCase()) {
                is ActionResult.Success -> {
                    _allPreviews.value = result.data
                    resetShownEntries()
                }

                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
            _isRefreshing.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = logoutUseCase()) {
                is ActionResult.Success -> {
                    resetState()
                }

                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
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

    fun fetchPreviews(decryptedKey: DecryptedKey?) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            if (decryptedKey == null) {
                Log.e("Unlock Vault", "Nonexistent vault key")
                _vaultState.value = VaultState.Error(
                    ErrorInfo(
                        type = ErrorType.INTERNAL,
                        source = "Unlock Vault",
                        message = "Nonexistent vault key"
                    )
                )
                return@launch
            }
            vaultKey = decryptedKey
            when (val result = getAllVaultEntryPreviewsUseCase()) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _allPreviews.value = result.data
                    resetShownEntries()
                }

                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
        }
    }

    fun fetchEntryDetails(id: Long) {
        viewModelScope.launch {
            when (val result = getDecryptedVaultEntryUseCase(id, vaultKey)) {
                is ActionResult.Success -> {
                    _expandedEntriesMap.update { it + (id to result.data) }
                }
                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
        }
    }

    fun clearEntryDetails(id: Long) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            _expandedEntriesMap.update { it + (id to null) }
            _vaultState.value = VaultState.Unlocked
        }
    }

    fun addEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = addEntryUseCase(decryptedVaultEntry, vaultKey)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _allPreviews.update { currentList ->
                        currentList + result.data
                    }
                    resetShownEntries()
                }

                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = deleteEntryUseCase(id)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _allPreviews.update { currentList ->
                        currentList.filter { it.id != id }
                    }
                    resetShownEntries()
                }

                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
        }
    }

    fun updateEntry(id: Long, decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = updateEntryUseCase(id, decryptedVaultEntry, vaultKey)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    _allPreviews.update { currentList ->
                        currentList.map {
                            if (it.id == id)
                                it.copy(title = decryptedVaultEntry.title)
                            else it
                        }
                    }
                    resetShownEntries()
                }

                is ActionResult.Error -> {
                    _vaultState.value = VaultState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                    Log.e(result.source, result.message)
                }
            }
        }
    }

    fun resetState() {
        _allPreviews.value = emptyList()
        _filteredPreviews.value = emptyList()
        _expandedEntriesMap.value = emptyMap()
        vaultKey = DecryptedKey(ByteArray(0))
        _vaultState.value = VaultState.Locked
    }

    private fun resetShownEntries() {
        _filteredPreviews.value = _allPreviews.value
        _expandedEntriesMap.value = _allPreviews.value.associate { it.id to null }
    }
}