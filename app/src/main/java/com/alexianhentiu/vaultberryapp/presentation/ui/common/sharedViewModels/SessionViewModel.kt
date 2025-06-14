package com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.AuthCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.ActivationSendUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.presentation.ui.common.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val activateSendUseCase: ActivationSendUseCase
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.LoggedOut)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _decryptedKey = MutableStateFlow<ByteArray>(ByteArray(0))
    val decryptedKey: StateFlow<ByteArray> = _decryptedKey

    private val _credentialsEvent = Channel<AuthCredentials>()
    val credentialsEvent = _credentialsEvent.receiveAsFlow()

    private val _tempEmail = MutableStateFlow<String>("")
    private val _tempPassword = MutableStateFlow<String>("")

    fun login(email: String?, password: String?, totpCode: String? = null) {
        viewModelScope.launch {
            _sessionState.value = SessionState.Loading
            when (val result = loginUseCase(
                email = email ?: _tempEmail.value,
                password = password ?: _tempPassword.value,
                totpCode = totpCode
            )) {
                is UseCaseResult.Success -> {
                    _sessionState.value = SessionState.LoggedIn
                    _decryptedKey.value = result.data
                    _credentialsEvent.send(
                        AuthCredentials(
                            email ?: _tempEmail.value,
                            password ?: _tempPassword.value
                        )
                    )
                    clearTempData()
                }

                is UseCaseResult.Error -> {  // Handle special cases
                    when (result.info.type) {
                        ErrorType.ACTIVATION_REQUIRED -> {
                            when (val activationResult = activateSendUseCase(email!!)) {
                                is UseCaseResult.Success -> {
                                    _sessionState.value = SessionState.ActivationEmailSent
                                }
                                is UseCaseResult.Error -> {
                                    _sessionState.value = SessionState.Error(activationResult.info)
                                }
                            }
                        }

                        ErrorType.TWO_FACTOR_REQUIRED -> {
                            _sessionState.value = SessionState.TwoFactorRequired
                            _tempEmail.value = email ?: _tempEmail.value
                            _tempPassword.value = password ?: _tempPassword.value
                        }

                        else -> {
                            _sessionState.value = SessionState.Error(result.info)
                            clearTempData()
                        }

                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _sessionState.value = SessionState.Loading
            when (val result = logoutUseCase()) {
                is UseCaseResult.Success -> {
                    resetState()
                }

                is UseCaseResult.Error -> {
                    if (result.info.type == ErrorType.LOGGED_OUT) {  // Already logged out
                        resetState()
                    } else {
                        _sessionState.value = SessionState.Error(result.info)
                    }
                }
            }
        }
    }

    fun resetState() {
        _sessionState.value = SessionState.LoggedOut
        clearTempData()
        _decryptedKey.value.fill(0)
    }

    /**
     * Called when the ViewModel is being destroyed.
     * Ensures the master key is cleared from memory.
     */
    override fun onCleared() {
        super.onCleared()
        resetState()
    }

    private fun clearTempData() {
        _tempEmail.value = ""
        _tempPassword.value = ""
    }
}