package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    val inputValidator: InputValidator
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String, firstName: String?, lastName: String?) {
        val account = Account(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            when (val result = registerUseCase(account)) {
                is APIResult.Success -> {
                    _registerState.value = RegisterState.Success
                    Log.d("RegisterViewModel", "API success: ${result.data}")
                }

                is APIResult.Error -> {
                    _registerState.value = RegisterState.Error(result.message)
                    Log.e("RegisterViewModel", "Registration failed: ${result.message}")
                }
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}