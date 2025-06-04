package com.alexianhentiu.vaultberryapp.presentation.viewmodel.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.account.AccountScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val disable2FAUseCase: Disable2FAUseCase
) : ViewModel() {

    private val _accountScreenState = MutableStateFlow<AccountScreenState>(AccountScreenState.Init)
    val accountScreenState: StateFlow<AccountScreenState> = _accountScreenState

    private val _accountInfo = MutableStateFlow<AccountInfo>(AccountInfo("", null, null))
    val accountInfo: StateFlow<AccountInfo> = _accountInfo

    private val _is2FAEnabled = MutableStateFlow<Boolean>(false)
    val is2FAEnabled: StateFlow<Boolean> = _is2FAEnabled

    private val _secretKeyEvent = Channel<String>()
    val secretKeyEvent = _secretKeyEvent.receiveAsFlow()

    private val _recoveryPasswordEvent = Channel<String>()
    val recoveryPasswordEvent = _recoveryPasswordEvent.receiveAsFlow()

    fun getAccountInfo() {
        viewModelScope.launch {
            _accountScreenState.value = AccountScreenState.Loading
            when (val resultAccount = getAccountInfoUseCase()) {
                is UseCaseResult.Success -> {
                    _accountInfo.value = resultAccount.data
                    when (val result2FA = get2FAStatusUseCase()) {
                        is UseCaseResult.Success -> {
                            _is2FAEnabled.value = result2FA.data
                            _accountScreenState.value = AccountScreenState.Idle
                        }

                        is UseCaseResult.Error -> {
                            _accountScreenState.value = AccountScreenState.Error(
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
                    _accountScreenState.value = AccountScreenState.Error(
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
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = deleteAccountUseCase()) {
                is UseCaseResult.Success -> {
                    clearData()
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(
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

    fun changeAccountInfo(
        email: String?,
        firstName: String?,
        lastName: String?,
        noActivation: Boolean
    ) {
        viewModelScope.launch {
            _accountScreenState.value = AccountScreenState.Loading

            val newAccountInfo =  _accountInfo.value.copy(
                firstName = firstName,
                lastName = lastName,
                email = email ?: _accountInfo.value.email
            )

            when (val result = changeAccountInfoUseCase(newAccountInfo, noActivation)) {
                is UseCaseResult.Success -> {
                    if (_accountInfo.value.email != newAccountInfo.email) {
                        _accountScreenState.value = AccountScreenState.ChangedEmail
                    } else {
                        _accountScreenState.value = AccountScreenState.Idle
                    }
                    _accountInfo.value = newAccountInfo
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(
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
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword, reEncrypt)) {
                is UseCaseResult.Success -> {
                    _accountScreenState.value = AccountScreenState.ChangedPassword
                    _recoveryPasswordEvent.send(result.data)
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(
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
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = setup2FAUseCase()) {
                is UseCaseResult.Success -> {
                    _accountScreenState.value = AccountScreenState.Setup2FA
                    _secretKeyEvent.send(result.data)
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(
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
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = disable2FAUseCase()) {
                is UseCaseResult.Success -> {
                    _accountScreenState.value = AccountScreenState.Idle
                    _is2FAEnabled.value = false
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(
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

    fun setLoadingState() {
        _accountScreenState.value = AccountScreenState.Loading
    }

    fun resetState() {
        _accountScreenState.value = AccountScreenState.Init
        clearData()
    }

    fun clearData() {
        _accountInfo.value = AccountInfo("", null, null)
        _is2FAEnabled.value = false
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }
}