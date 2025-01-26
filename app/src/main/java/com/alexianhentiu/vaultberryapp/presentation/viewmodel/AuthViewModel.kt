package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
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
    val inputValidator: InputValidator
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState

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

    fun resetState() {
        _authState.value = AuthState.LoggedOut
    }
}