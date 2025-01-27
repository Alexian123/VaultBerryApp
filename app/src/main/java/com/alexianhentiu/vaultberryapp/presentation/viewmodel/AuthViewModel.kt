package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.GetRecoveryKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoveryLoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val decryptKeyUseCase: DecryptKeyUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getRecoveryKeyUseCase: GetRecoveryKeyUseCase,
    private val recoveryLoginUseCase: RecoveryLoginUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState

    fun recoveryLogin(email: String, recoveryPassword: String) {
        viewModelScope.launch {
            when (val result1 = getRecoveryKeyUseCase(email)) {
                is APIResult.Success -> {
                    _authState.value = AuthState.Loading
                    val recoveryKey = result1.data
                    val loginCredentials = LoginCredentials(email, recoveryKey.oneTimePassword)

                    when (val result2 = recoveryLoginUseCase(loginCredentials)) {
                        is APIResult.Success -> {
                            val decryptedRecoveryKey = decryptKeyUseCase(recoveryPassword,
                                recoveryKey.salt, recoveryKey.key)
                            _authState.value = AuthState.LoggedIn(decryptedRecoveryKey,
                                true)
                            Log.d("LoginViewModel", "API success: ${result2.data}")
                        }

                        is APIResult.Error -> {
                            _authState.value = AuthState.Error(result2.message)
                            Log.e("LoginViewModel",
                                "Recovery login failed: ${result2.message}")
                        }
                    }
                }

                is APIResult.Error -> {
                    _authState.value = AuthState.Error(result1.message)
                    Log.e("LoginViewModel", "Get recovery key failed: ${result1.message}")
                }
            }
        }
    }

    fun login(email: String, password: String) {
        val loginCredentials = LoginCredentials(email, password)
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000)
            when (val result = loginUseCase(loginCredentials)) {
                is APIResult.Success -> {
                    val keyChain = result.data
                    val decryptedVaultKey = decryptKeyUseCase(password, keyChain.salt,
                        keyChain.vaultKey)
                    _authState.value = AuthState.LoggedIn(decryptedVaultKey)
                }

                is APIResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                    Log.e("LoginViewModel", "Login failed: ${result.message}")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = logoutUseCase()) {
                is APIResult.Success -> {
                    _authState.value = AuthState.LoggedOut
                    Log.d("LoginViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                    Log.e("LoginViewModel", "Logout failed: ${result.message}")
                }
            }
        }
    }

    fun forgotPassword() {
        _authState.value = AuthState.ForgotPassword
    }

    fun resetState() {
        _authState.value = AuthState.LoggedOut
    }
}