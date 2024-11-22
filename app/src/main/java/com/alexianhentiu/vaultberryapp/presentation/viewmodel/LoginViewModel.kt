package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.usecase.LoginUseCase
import com.alexianhentiu.vaultberryapp.presentation.ui.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        val loginCredentials = LoginCredentials(email, password)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            when (val result = loginUseCase(loginCredentials)) {
                is APIResult.Success -> {
                    _loginState.value = LoginState.Success(result.data)
                }
                is APIResult.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                }
            }
        }
    }
}