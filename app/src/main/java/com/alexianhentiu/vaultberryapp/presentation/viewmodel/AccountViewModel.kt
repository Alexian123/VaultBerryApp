package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeEmailUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeNameUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val changeEmailUseCase: ChangeEmailUseCase,
    private val changeNameUseCase: ChangeNameUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _accountState = MutableStateFlow<AccountState>(AccountState.Init)
    val accountState: StateFlow<AccountState> = _accountState

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> = _account

    fun getAccount() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = getAccountUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle
                    _account.value = result.data
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = deleteAccountUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Deleted
                    Log.d("AccountViewModel", "API success: ${result.data}")
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Delete failed: ${result.message}")
                }
            }
        }
    }

    fun changeEmail(newEmail: String) {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = changeEmailUseCase(newEmail)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle
                    Log.d("AccountViewModel", "API success: ${result.data}")
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Change email failed: ${result.message}")
                }
            }
        }
    }

    fun changeName(firstName: String?, lastName: String?) {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = changeNameUseCase(firstName, lastName)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle
                    Log.d("AccountViewModel", "API success: ${result.data}")
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Change name failed: ${result.message}")
                }
            }
        }
    }

    fun changePassword(decryptedKey: DecryptedKey, newPassword: String) {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.ChangedPassword(result.data)
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Change password failed: ${result.message}")
                }
            }
        }
    }

    fun resetState() {
        _accountState.value = AccountState.Init
    }
}