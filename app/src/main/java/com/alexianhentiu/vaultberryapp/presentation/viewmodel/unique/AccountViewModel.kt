package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.presentation.utils.errors.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val get2FAStatusUseCase: Get2FAStatusUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val changeAccountInfoUseCase: ChangeAccountInfoUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val setup2FAUseCase: Setup2FAUseCase,
    private val disable2FAUseCase: Disable2FAUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _accountState = MutableStateFlow<AccountState>(AccountState.Init)
    val accountState: StateFlow<AccountState> = _accountState

    fun getAccountInfo() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val resultAccount = getAccountInfoUseCase()) {
                is UseCaseResult.Success -> {

                    when (val result2FA = get2FAStatusUseCase()) {
                        is UseCaseResult.Success -> {
                            _accountState.value = AccountState.Idle(
                                resultAccount.data,
                                result2FA.data
                            )
                        }

                        is UseCaseResult.Error -> {
                            _accountState.value = AccountState.Error(
                                ErrorInfo(
                                    type = result2FA.type,
                                    source = result2FA.source,
                                    message = result2FA.message
                                )
                            )
                            Log.e(result2FA.source, result2FA.message)
                        }
                    }
                }

                is UseCaseResult.Error -> {
                    _accountState.value = AccountState.Error(
                        ErrorInfo(
                            type = resultAccount.type,
                            source = resultAccount.source,
                            message = resultAccount.message
                        )
                    )
                    Log.e(resultAccount.source, resultAccount.message)
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = deleteAccountUseCase()) {
                is UseCaseResult.Success -> {
                    _accountState.value = AccountState.LoggedOut
                }

                is UseCaseResult.Error -> {
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

    fun changeAccountInfo(email: String?, firstName: String?, lastName: String?) {
        viewModelScope.launch {
            val savedState = (_accountState.value as AccountState.Idle)
            _accountState.value = AccountState.Loading

            val accountInfo = AccountInfo(
                email = email ?: savedState.accountInfo.email,
                firstName = firstName,
                lastName = lastName
            )
            when (val result = changeAccountInfoUseCase(accountInfo)) {
                is UseCaseResult.Success -> {
                    _accountState.value = AccountState.Idle(
                        accountInfo.copy(
                            firstName = accountInfo.firstName ?: savedState.accountInfo.firstName,
                            lastName = accountInfo.lastName ?: savedState.accountInfo.lastName
                        ),
                        savedState.is2FAEnabled
                    )
                }

                is UseCaseResult.Error -> {
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

    fun changePassword(decryptedKey: ByteArray, newPassword: String, reEncrypt: Boolean) {
        viewModelScope.launch {
            _accountState.value = AccountState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword, reEncrypt)) {
                is UseCaseResult.Success -> {
                    _accountState.value = AccountState.ChangedPassword(result.data)
                }

                is UseCaseResult.Error -> {
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
                is UseCaseResult.Success -> {
                    _accountState.value = AccountState.Setup2FA(result.data)
                }

                is UseCaseResult.Error -> {
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
                is UseCaseResult.Success -> {
                    _accountState.value = AccountState.Idle(
                        savedState.accountInfo,
                        false
                    )
                }

                is UseCaseResult.Error -> {
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
                is UseCaseResult.Success -> {
                    _accountState.value = AccountState.LoggedOut
                }

                is UseCaseResult.Error -> {
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