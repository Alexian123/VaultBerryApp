package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.KeyImportUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val keyImportUseCase: KeyImportUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        val loginCredentials = LoginCredentials(email, password)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000)
            when (val result = loginUseCase(loginCredentials)) {
                is APIResult.Success -> {
                    val decryptedVaultKey = keyImportUseCase(password, result.data)
                    _loginState.value = LoginState.Success(decryptedVaultKey)
                }

                is APIResult.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                }
            }
        }
    }
}