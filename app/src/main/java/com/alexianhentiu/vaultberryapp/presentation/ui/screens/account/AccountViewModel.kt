package com.alexianhentiu.vaultberryapp.presentation.ui.screens.account

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Activate2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.DeleteAccountUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Get2FAStatusUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Setup2FAUseCase
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
    private val activate2FAUseCase: Activate2FAUseCase,
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

    private val _qrBitmap = MutableStateFlow<ImageBitmap?>(null)
    val qrBitmap: StateFlow<ImageBitmap?> = _qrBitmap

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
                            _accountScreenState.value = AccountScreenState.Error(result2FA.info)
                        }
                    }
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(resultAccount.info)
                }
            }
        }
    }

    fun deleteAccount(password: String) {
        viewModelScope.launch {
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = deleteAccountUseCase(password)) {
                is UseCaseResult.Success -> {
                    _accountScreenState.value = AccountScreenState.DeletedAccount
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(result.info)
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
                    _accountScreenState.value = AccountScreenState.Error(result.info)
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
                    _accountScreenState.value = AccountScreenState.Error(result.info)
                }
            }
        }
    }

    fun setup2FA() {
        viewModelScope.launch {
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = setup2FAUseCase()) {
                is UseCaseResult.Success -> {
                    val totpData = result.data
                    _secretKey.value = totpData.secret
                    try {
                        val imageBytes = totpData.qrCodeBytes
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        _qrBitmap.value = bitmap?.asImageBitmap()
                    } catch (_: Exception) {
                        _qrBitmap.value = null
                    } finally {
                        _accountScreenState.value = AccountScreenState.Setup2FA
                    }
                }

                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(result.info)
                }
            }
        }
    }

    fun activate2FA(code: String) {
        viewModelScope.launch {
            _accountScreenState.value = AccountScreenState.Loading
            when (val result = activate2FAUseCase(code)) {
                is UseCaseResult.Success -> {
                    _accountScreenState.value = AccountScreenState.Activated2FA
                    _is2FAEnabled.value = true
                }
                is UseCaseResult.Error -> {
                    _accountScreenState.value = AccountScreenState.Error(result.info)
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
                    _accountScreenState.value = AccountScreenState.Error(result.info)
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