package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.GetRecoveryOTPUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.states.RecoveryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val getRecoveryOTPUseCase: GetRecoveryOTPUseCase,
    private val recoveryLoginUseCase: RecoveryLoginUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _recoveryState = MutableStateFlow<RecoveryState>(RecoveryState.Idle)
    val recoveryState: StateFlow<RecoveryState> = _recoveryState

    fun requestOTP(email: String) {
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = getRecoveryOTPUseCase(email)) {
                is ActionResult.Success -> {
                    _recoveryState.value = RecoveryState.OTPRequested(email)
                }

                is ActionResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(
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

    fun recoveryLogin(email: String, recoveryPassword: String, otp: String) {
        val credentials = LoginCredentials(
            email = email,
            password = recoveryPassword,
            token = otp
        )
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = recoveryLoginUseCase(credentials)) {
                is ActionResult.Success -> {
                    _recoveryState.value = RecoveryState.LoggedIn(result.data)
                }

                is ActionResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(
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

    fun resetPassword(decryptedKey: DecryptedKey, newPassword: String) {
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword)) {
                is ActionResult.Success -> {
                    _recoveryState.value = RecoveryState.PasswordReset(result.data)
                }

                is ActionResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(
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
            _recoveryState.value = RecoveryState.Loading
            when (val result = logoutUseCase()) {
                is ActionResult.Success -> {
                    _recoveryState.value = RecoveryState.Idle
                }

                is ActionResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(
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
        _recoveryState.value = RecoveryState.Idle
    }
}