package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.KeyExportUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.presentation.ui.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val keyExportUseCase: KeyExportUseCase,
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String, firstName: String?, lastName: String?) {
        // TODO: Generate recovery key
        val exportedVaultKey = keyExportUseCase(password)
        val vaultKey = exportedVaultKey.ivAndKey
        val salt = exportedVaultKey.salt
        val user = User(email, password, salt, vaultKey, vaultKey, firstName, lastName)

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            when (val result = registerUseCase(user)) {
                is APIResult.Success -> {
                    _registerState.value = RegisterState.Success
                }

                is APIResult.Error -> {
                    _registerState.value = RegisterState.Error(result.message)
                }
            }
        }
    }
}