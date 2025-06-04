package com.alexianhentiu.vaultberryapp.presentation.ui.screens.recovery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoverySendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val recoverySendUseCase: RecoverySendUseCase,
    private val recoveryLoginUseCase: RecoveryLoginUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _recoveryScreenState =
        MutableStateFlow<RecoveryScreenState>(RecoveryScreenState.Idle)
    val recoveryScreenState: StateFlow<RecoveryScreenState> = _recoveryScreenState

    private val _tempEmail = MutableStateFlow<String>("")
    private val _tempDecryptedKey = MutableStateFlow<ByteArray>(ByteArray(0))

    private val _otpRequested = MutableStateFlow<Boolean>(false)
    val otpRequested: StateFlow<Boolean> = _otpRequested

    private val _recoveryPasswordEvent = Channel<String>()
    val recoveryPasswordEvent = _recoveryPasswordEvent.receiveAsFlow()

    fun requestOTP(email: String) {
        viewModelScope.launch {
            _recoveryScreenState.value = RecoveryScreenState.Loading
            _otpRequested.value = true
            _tempEmail.value = email
            when (val result = recoverySendUseCase(email)) {
                is UseCaseResult.Success -> {
                    _recoveryScreenState.value = RecoveryScreenState.Idle
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

    fun recoveryLogin(recoveryPassword: String, otp: String) {
        viewModelScope.launch {
            _recoveryScreenState.value = RecoveryScreenState.Loading
            when (val result = recoveryLoginUseCase(_tempEmail.value, recoveryPassword, otp)) {
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
            when (val result = changePasswordUseCase(
                _tempDecryptedKey.value,
                newPassword, reEncrypt
            )) {
                is UseCaseResult.Success -> {
                    _recoveryScreenState.value = RecoveryScreenState.PasswordReset
                    _recoveryPasswordEvent.send(result.data)
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
        _tempDecryptedKey.value.fill(0)
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }
}