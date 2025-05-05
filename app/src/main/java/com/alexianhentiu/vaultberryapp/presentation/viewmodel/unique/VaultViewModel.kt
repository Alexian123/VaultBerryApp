package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.vault.DeleteEntryUseCase
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
    private val getEntriesUseCase: GetEntriesUseCase,
    private val addEntryUseCase: AddEntryUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    private val updateEntryUseCase: UpdateEntryUseCase,
    val inputValidator: InputValidator,
    val passwordEvaluator: PasswordEvaluator
): ViewModel() {

    private val _vaultState = MutableStateFlow<VaultState>(VaultState.Locked)
    val vaultState: StateFlow<VaultState> = _vaultState

    private val allEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    private val _filteredEntries = MutableStateFlow<List<DecryptedVaultEntry>>(emptyList())
    val filteredEntries: StateFlow<List<DecryptedVaultEntry>> = _filteredEntries

    private lateinit var vaultKey: DecryptedKey

    fun logout() {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = logoutUseCase()) {
                is ActionResult.Success -> {
                    allEntries.value = emptyList()
                    _filteredEntries.value = emptyList()
                    vaultKey = DecryptedKey(ByteArray(0))
                    _vaultState.value = VaultState.Locked
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
            when (val result = getEntriesUseCase(vaultKey)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.value = result.data
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

    fun addEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = addEntryUseCase(decryptedVaultEntry, vaultKey)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.update { currentList ->
                        currentList + decryptedVaultEntry
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

    fun deleteEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = deleteEntryUseCase(decryptedVaultEntry)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.update { currentList ->
                        currentList.filter { it.timestamp != decryptedVaultEntry.timestamp }
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

    fun updateEntry(decryptedVaultEntry: DecryptedVaultEntry) {
        viewModelScope.launch {
            _vaultState.value = VaultState.Loading
            when (val result = updateEntryUseCase(decryptedVaultEntry, vaultKey)) {
                is ActionResult.Success -> {
                    _vaultState.value = VaultState.Unlocked
                    allEntries.update { currentList ->
                        currentList.map {
                            if (it.timestamp == decryptedVaultEntry.timestamp)
                                decryptedVaultEntry
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
        _vaultState.value = VaultState.Locked
    }

    private fun resetShownEntries() {
        _filteredEntries.value = allEntries.value
    }
}