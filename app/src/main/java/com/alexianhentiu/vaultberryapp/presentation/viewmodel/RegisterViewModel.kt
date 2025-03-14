package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.usecase.core.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.states.RegisterState
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
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            when (val result = registerUseCase(account, password)) {
                is ActionResult.Success -> {
                    _registerState.value = RegisterState.Success(result.data)
                }

                is ActionResult.Error -> {
                    _registerState.value = RegisterState.Error(
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
        _registerState.value = RegisterState.Idle
    }
}