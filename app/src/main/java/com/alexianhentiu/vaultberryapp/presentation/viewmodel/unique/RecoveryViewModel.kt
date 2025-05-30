package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RecoverySendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.presentation.utils.store.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RecoveryScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val recoverySendUseCase: RecoverySendUseCase,
    private val recoveryLoginUseCase: RecoveryLoginUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _recoveryScreenState = MutableStateFlow<RecoveryScreenState>(RecoveryScreenState.Idle)
    val recoveryScreenState: StateFlow<RecoveryScreenState> = _recoveryScreenState

    private val _tempEmail = MutableStateFlow<String>("")
    private val _tempDecryptedKey = MutableStateFlow<ByteArray>(ByteArray(0))

    private val _tempRecoveryPassword = MutableStateFlow<String>("")
    val tempRecoveryPassword: StateFlow<String> = _tempRecoveryPassword

    fun requestOTP(email: String) {
        viewModelScope.launch {
            _recoveryScreenState.value = RecoveryScreenState.Loading
            when (val result = recoverySendUseCase(email)) {
                is UseCaseResult.Success -> {
                    _recoveryScreenState.value = RecoveryScreenState.OTPRequested
                    _tempEmail.value = email
                }

                is UseCaseResult.Error -> {
                    _recoveryScreenState.value = RecoveryScreenState.Error(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun recoveryLogin(email: String?, recoveryPassword: String, otp: String) {
        viewModelScope.launch {
            _recoveryScreenState.value = RecoveryScreenState.Loading
            when (val result = recoveryLoginUseCase(email ?: _tempEmail.value, recoveryPassword, otp)) {
                is UseCaseResult.Success -> {
                    _recoveryScreenState.value = RecoveryScreenState.LoggedIn
                    _tempDecryptedKey.value = result.data
                }

                is UseCaseResult.Error -> {
                    _recoveryScreenState.value = RecoveryScreenState.Error(
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

    fun resetPassword(newPassword: String, reEncrypt: Boolean) {
        viewModelScope.launch {
            _recoveryScreenState.value = RecoveryScreenState.Loading
            when (val result = changePasswordUseCase(_tempDecryptedKey.value, newPassword, reEncrypt)) {
                is UseCaseResult.Success -> {
                    _recoveryScreenState.value = RecoveryScreenState.PasswordReset
                    _tempRecoveryPassword.value = result.data
                }

                is UseCaseResult.Error -> {
                    _recoveryScreenState.value = RecoveryScreenState.Error(
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
        _recoveryScreenState.value = RecoveryScreenState.Idle
        clearData()
    }

    fun clearData() {
        _tempEmail.value = ""
        _tempDecryptedKey.value.fill(0)
        _tempRecoveryPassword.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }
}