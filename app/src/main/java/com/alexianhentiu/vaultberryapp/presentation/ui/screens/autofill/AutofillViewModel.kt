package com.alexianhentiu.vaultberryapp.presentation.ui.screens.autofill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AutofillEntry
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.SearchVaultEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutofillViewModel @Inject constructor(
    private val searchVaultEntriesUseCase: SearchVaultEntriesUseCase
) : ViewModel() {

    private val _autofillSuggestions = MutableStateFlow<List<AutofillEntry>>(emptyList())
    val autofillSuggestions: StateFlow<List<AutofillEntry>> = _autofillSuggestions

    private val _state = MutableStateFlow<AutofillState>(AutofillState.Idle)
    val state: StateFlow<AutofillState> = _state

    fun searchVaultEntries(keywords: List<String>, decryptedKey: ByteArray) {
        viewModelScope.launch {
            _state.value = AutofillState.Loading
            when (val result = searchVaultEntriesUseCase(keywords, decryptedKey)) {
                is UseCaseResult.Success -> {
                    _autofillSuggestions.value = result.data.map { decryptedVaultEntry ->
                        AutofillEntry(
                            username = decryptedVaultEntry.username,
                            password = decryptedVaultEntry.password
                        )
                    }
                    _state.value = AutofillState.Success
                }

                is UseCaseResult.Error -> {
                    _state.value = AutofillState.Error(result.info)
                }
            }
        }
    }

    fun clearSuggestions() {
        _autofillSuggestions.value = emptyList()
    }

    fun resetState() {
        _state.value = AutofillState.Idle
        clearSuggestions()
    }

    override fun onCleared() {
        super.onCleared()
        _autofillSuggestions.value = emptyList()
    }
}