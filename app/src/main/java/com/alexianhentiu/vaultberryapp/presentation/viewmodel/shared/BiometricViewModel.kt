package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricAuthManager
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricStatus
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.AuthCredentials
import com.alexianhentiu.vaultberryapp.presentation.utils.state.BiometricState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {
    private val _biometricState = MutableStateFlow<BiometricState>(BiometricState.Idle)
    val biometricState: StateFlow<BiometricState> = _biometricState

    private val _hasStoredCredentials = MutableStateFlow(false)
    val hasStoredCredentials: StateFlow<Boolean> = _hasStoredCredentials

    private val _credentials = MutableStateFlow<AuthCredentials?>(null)
    val credentials: StateFlow<AuthCredentials?> = _credentials

    fun checkStoredCredentials() {
        _hasStoredCredentials.value = biometricAuthManager.hasStoredCredentials()
    }

    fun storeCredentials(
        activity: FragmentActivity,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _biometricState.value = BiometricState.Loading
            biometricAuthManager.storeCredentialsWithBiometric(
                activity = activity,
                email = email,
                password = password,
                onSuccess = {
                    _biometricState.value = BiometricState.CredentialsStored
                    _hasStoredCredentials.value = true
                },
                onError = { info ->
                    _biometricState.value = BiometricState.Error(info)
                }
            )
        }
    }

    fun authenticate(activity: FragmentActivity) {
        viewModelScope.launch {
            _biometricState.value = BiometricState.Loading
            when (val result = biometricAuthManager.isBiometricAvailable(activity)) {
                BiometricStatus.Available -> {
                    biometricAuthManager.retrieveCredentialsWithBiometric(
                        activity = activity,
                        onSuccess = { credentials ->
                            _biometricState.value = BiometricState.Authenticated
                            _credentials.value = credentials
                        },
                        onError = { errorInfo ->
                            _biometricState.value = BiometricState.Error(errorInfo)
                        }
                    )
                }

                else -> {
                    val errorInfo = (result as BiometricStatus.Error).info
                    _biometricState.value = BiometricState.Error(errorInfo)
                }
            }
        }
    }

    fun clearStoredCredentials() {
        biometricAuthManager.clearStoredCredentials()
    }
}