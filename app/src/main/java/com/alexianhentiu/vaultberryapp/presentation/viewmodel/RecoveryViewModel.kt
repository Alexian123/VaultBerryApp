package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.core.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.GetRecoveryOTPUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.RecoveryState
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
                is APIResult.Success -> {
                    _recoveryState.value = RecoveryState.OTPRequested(email)
                }

                is APIResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(result.message)
                }
            }
        }
    }

    fun recoveryLogin(email: String, otp: String, recoveryPassword: String) {
        val loginCredentials = LoginCredentials(email, otp)
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = recoveryLoginUseCase(loginCredentials, recoveryPassword)) {
                is APIResult.Success -> {
                    _recoveryState.value = RecoveryState.LoggedIn(result.data)
                }

                is APIResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(result.message)
                    Log.e("RecoveryViewModel", "Recovery login failed: ${result.message}")
                }
            }
        }
    }

    fun resetPassword(decryptedKey: DecryptedKey, newPassword: String) {
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword)) {
                is APIResult.Success -> {
                    _recoveryState.value = RecoveryState.PasswordReset(result.data)
                }

                is APIResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(result.message)
                    Log.e("AccountViewModel", "Change password failed: ${result.message}")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = logoutUseCase()) {
                is APIResult.Success -> {
                    _recoveryState.value = RecoveryState.Idle
                    Log.d("RecoveryViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _recoveryState.value = RecoveryState.Error(result.message)
                    Log.e("RecoveryViewModel", "Logout failed: ${result.message}")
                }
            }
        }
    }

    fun resetState() {
        _recoveryState.value = RecoveryState.Idle
    }
}