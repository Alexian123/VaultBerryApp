package com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.presentation.utils.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String, firstName: String?, lastName: String?) {
        val accountInfo = AccountInfo(
            email = email,
            firstName = firstName,
            lastName = lastName
        )
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            when (val result = registerUseCase(accountInfo, password)) {
                is UseCaseResult.Success -> {
                    _registerState.value = RegisterState.Success(result.data)
                }

                is UseCaseResult.Error -> {
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