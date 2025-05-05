package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.usecase.general.account.ChangePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.RecoverySendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.general.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.states.RecoveryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val recoverySendUseCase: RecoverySendUseCase,
    private val recoveryLoginUseCase: RecoveryLoginUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
    val inputValidator: InputValidator,
    val passwordEvaluator: PasswordEvaluator
) : ViewModel() {

    private val _recoveryState = MutableStateFlow<RecoveryState>(RecoveryState.Idle)
    val recoveryState: StateFlow<RecoveryState> = _recoveryState

    fun requestOTP(email: String) {
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = recoverySendUseCase(email)) {
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
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = recoveryLoginUseCase(email, recoveryPassword, otp)) {
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

    fun resetPassword(decryptedKey: DecryptedKey, newPassword: String, reEncrypt: Boolean) {
        viewModelScope.launch {
            _recoveryState.value = RecoveryState.Loading
            when (val result = changePasswordUseCase(decryptedKey, newPassword, reEncrypt)) {
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