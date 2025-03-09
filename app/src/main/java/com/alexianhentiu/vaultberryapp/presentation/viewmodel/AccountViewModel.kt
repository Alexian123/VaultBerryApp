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
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Setup2FAUseCase
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
    private val get2FAStatusUseCase: Get2FAStatusUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val changeEmailUseCase: ChangeEmailUseCase,
    private val changeNameUseCase: ChangeNameUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val setup2FAUseCase: Setup2FAUseCase,
    private val disable2FAUseCase: Disable2FAUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _accountState = MutableStateFlow<AccountState>(AccountState.Init)
    val accountState: StateFlow<AccountState> = _accountState

    fun getAccountInfo() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val resultAccount = getAccountUseCase()) {
                is ActionResult.Success -> {

                    when (val result2FA = get2FAStatusUseCase()) {
                        is ActionResult.Success -> {
                            _accountState.value = AccountState.Idle(
                                resultAccount.data,
                                result2FA.data
                            )
                        }

                        is ActionResult.Error -> {
                            _accountState.value = AccountState.Error(result2FA.message)
                        }
                    }
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(resultAccount.message)
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
            val savedState = (_accountState.value as AccountState.Idle)
            _accountState.value = AccountState.Loading
            when (val result = changeEmailUseCase(newEmail)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle(
                        savedState.account.copy(email = newEmail),
                        savedState.is2FAEnabled
                    )
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
            val savedState = (_accountState.value as AccountState.Idle)
            _accountState.value = AccountState.Loading
            when (val result = changeNameUseCase(firstName, lastName)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle(
                        savedState.account.copy(
                            firstName = firstName ?: savedState.account.firstName,
                            lastName = lastName ?: savedState.account.lastName
                        ),
                        savedState.is2FAEnabled
                    )
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

    fun setup2FA() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = setup2FAUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Setup2FA(result.data)
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Setup 2FA failed: ${result.message}")
                }
            }
        }
    }

    fun disable2FA() {
        viewModelScope.launch {
            val savedState = (_accountState.value as AccountState.Idle)
            _accountState.value = AccountState.Loading
            when (val result = disable2FAUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle(
                        savedState.account,
                        false
                    )
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(result.message)
                    Log.e("AccountViewModel", "Disable 2FA failed: ${result.message}")
                }
            }
        }
    }

    fun resetState() {
        _accountState.value = AccountState.Init
    }
}