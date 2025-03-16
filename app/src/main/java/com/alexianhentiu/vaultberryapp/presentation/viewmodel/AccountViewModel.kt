package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.GetAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.states.AccountState
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
    private val changeAccountInfoUseCase: ChangeAccountInfoUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val setup2FAUseCase: Setup2FAUseCase,
    private val disable2FAUseCase: Disable2FAUseCase,
    private val logoutUseCase: LogoutUseCase,
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
                            _accountState.value = AccountState.Error(
                                ErrorInfo(
                                    type = result2FA.type,
                                    source = result2FA.source,
                                    message = result2FA.message
                                )
                            )
                        }
                    }
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(
                        ErrorInfo(
                            type = resultAccount.type,
                            source = resultAccount.source,
                            message = resultAccount.message
                        )
                    )
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = deleteAccountUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.LoggedOut
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(
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

    fun changeInfo(email: String?, firstName: String?, lastName: String?) {
        viewModelScope.launch {
            val savedState = (_accountState.value as AccountState.Idle)
            _accountState.value = AccountState.Loading

            val accountInfo = Account(
                email = email ?: savedState.account.email,
                firstName = firstName,
                lastName = lastName
            )
            when (val result = changeAccountInfoUseCase(accountInfo)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Idle(
                        accountInfo.copy(
                            firstName = accountInfo.firstName ?: savedState.account.firstName,
                            lastName = accountInfo.lastName ?: savedState.account.lastName
                        ),
                        savedState.is2FAEnabled
                    )
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(
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

    fun changePassword(decryptedKey: DecryptedKey, newPassword: String) {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword)) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.ChangedPassword(result.data)
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(
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

    fun setup2FA() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = setup2FAUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.Setup2FA(result.data)
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(
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
                    _accountState.value = AccountState.Error(
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
            _accountState.value = AccountState.Loading
            when (val result = logoutUseCase()) {
                is ActionResult.Success -> {
                    _accountState.value = AccountState.LoggedOut
                }

                is ActionResult.Error -> {
                    _accountState.value = AccountState.Error(
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
        _accountState.value = AccountState.Init
    }
}