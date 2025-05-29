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
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.presentation.utils.store.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AccountScreenState
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
    private val disable2FAUseCase: Disable2FAUseCase
) : ViewModel() {

    private val _accountScreenState = MutableStateFlow<AccountScreenState>(AccountScreenState.Init)
    val accountScreenState: StateFlow<AccountScreenState> = _accountScreenState

    private val _accountInfo = MutableStateFlow<AccountInfo>(AccountInfo("", null, null))
    val accountInfo: StateFlow<AccountInfo> = _accountInfo

    private val _is2FAEnabled = MutableStateFlow<Boolean>(false)
    val is2FAEnabled: StateFlow<Boolean> = _is2FAEnabled

    private val _secretKey = MutableStateFlow<String>("")
    val secretKey: StateFlow<String> = _secretKey

    private val _recoveryPassword = MutableStateFlow<String>("")
    val recoveryPassword: StateFlow<String> = _recoveryPassword

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
                    _accountScreenState.value = AccountScreenState.Init
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

    fun changeAccountInfo(email: String?, firstName: String?, lastName: String?) {
        viewModelScope.launch {
            _accountScreenState.value = AccountScreenState.Loading

            val newAccountInfo =  _accountInfo.value.copy(
                firstName = firstName,
                lastName = lastName,
                email = email ?: _accountInfo.value.email
            )

            when (val result = changeAccountInfoUseCase(newAccountInfo)) {
                is UseCaseResult.Success -> {
                    _accountScreenState.value = AccountScreenState.Idle
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
                    _recoveryPassword.value = result.data
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
                    _secretKey.value = result.data
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
        _secretKey.value = ""
        _recoveryPassword.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }
}