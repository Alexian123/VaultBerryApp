package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.usecase.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.UpdateAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.UpdateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val generateKeyChainUseCase: GenerateKeyChainUseCase,
    private val decryptKeyUseCase: DecryptKeyUseCase,
    private val updateKeyChainUseCase: UpdateKeyChainUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _accountState = MutableStateFlow<AccountState>(AccountState.Idle)
    val accountState: StateFlow<AccountState> = _accountState

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> = _account

    fun getAccount() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = getAccountUseCase()) {
                is APIResult.Success -> {
                    _accountState.value = AccountState.Idle
                    _account.value = result.data
                }

                is APIResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                }
            }
        }
    }

    fun updateAccount(email: String, password: String?, firstName: String?, lastName: String?) {
        val account = Account(
            email = email,
            password = password ?: _account.value!!.password,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result1 = updateAccountUseCase(account)) {
                is APIResult.Success -> {
                    Log.d("AccountViewModel", "API success: ${result1.data}")

                    if (password != null) {
                        _accountState.value = AccountState.Loading
                        val keyChain = generateKeyChainUseCase(password, "test")
                        when (val result2 = updateKeyChainUseCase(keyChain)) {
                            is APIResult.Success -> {
                                val decryptedKey = decryptKeyUseCase(
                                    password = password,
                                    salt = keyChain.salt,
                                    encryptedKey = keyChain.vaultKey
                                )
                                _accountState.value = AccountState.UpdatedPassword(decryptedKey)
                                Log.d("AccountViewModel", "API success: ${result2.data}")
                            }

                            is APIResult.Error -> {
                                _accountState.value = AccountState.Error(result2.message)
                                Log.e("AccountViewModel",
                                    "Update failed: ${result2.message}")
                            }
                        }
                    } else {
                        _accountState.value = AccountState.Updated
                    }
                }

                is APIResult.Error -> {
                    _accountState.value = AccountState.Error(result1.message)
                    Log.e("AccountViewModel", "Update failed: ${result1.message}")
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = deleteAccountUseCase()) {
                is APIResult.Success -> {
                    _accountState.value = AccountState.Deleted
                    Log.d("AccountViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Delete failed: ${result.message}")
                }
            }
        }
    }

    fun forcePasswordReset() {
        getAccount()
        _accountState.value = AccountState.ForcedPasswordReset
    }

    fun resetState() {
        _account.value = null
        _accountState.value = AccountState.Init
    }
}