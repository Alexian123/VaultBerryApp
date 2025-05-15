package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.vault.SearchVaultEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.presentation.utils.autofill.AutofillEntry
import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AutofillState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutofillViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val searchVaultEntriesUseCase: SearchVaultEntriesUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _autofillState = MutableStateFlow<AutofillState>(AutofillState.Idle)
    val autofillState: StateFlow<AutofillState> = _autofillState

    private val _decryptedKey = MutableStateFlow<ByteArray?>(null)

    private val _autofillSuggestions = MutableStateFlow<List<AutofillEntry>>(emptyList())
    val autofillSuggestions: StateFlow<List<AutofillEntry>> = _autofillSuggestions

    fun login(email: String, password: String, code: String? = null) {
        viewModelScope.launch {
            _autofillState.value = AutofillState.Loading
            when (val result = loginUseCase(email, password, code)) {
                is UseCaseResult.Success -> {
                    _autofillState.value = AutofillState.LoggedIn
                    _decryptedKey.value = result.data
                }

                is UseCaseResult.Error -> {
                    if (result.message == "2FA required") {
                        _autofillState.value = AutofillState.Verify2FA
                    } else {
                        _autofillState.value = AutofillState.Error(
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
    }

    fun searchVaultEntries(keywords: List<String>) {
        viewModelScope.launch {
            _autofillState.value = AutofillState.Loading
            if (_decryptedKey.value == null) {
                _autofillState.value = AutofillState.Error(
                    ErrorInfo(
                        type = ErrorType.INTERNAL,
                        source = "AutofillViewModel",
                        message = "Decrypted key is null"
                    )
                )
                return@launch
            }

            when (val result = searchVaultEntriesUseCase(keywords, _decryptedKey.value!!)) {
                is UseCaseResult.Success -> {
                    _autofillSuggestions.value = result.data.map { decryptedVaultEntry ->
                        AutofillEntry(
                            username = decryptedVaultEntry.username,
                            password = decryptedVaultEntry.password
                        )
                    }
                    _autofillState.value = AutofillState.Success
                }

                is UseCaseResult.Error -> {
                    _autofillState.value = AutofillState.Error(
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

    fun logout() {
        viewModelScope.launch {
            _autofillState.value = AutofillState.Loading
            when (val result = logoutUseCase()) {
                is UseCaseResult.Success -> {
                    resetState()
                }

                is UseCaseResult.Error -> {
                    _autofillState.value = AutofillState.Error(
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
        _decryptedKey.value = null
        _autofillSuggestions.value = emptyList()
        _autofillState.value = AutofillState.Idle
    }
}