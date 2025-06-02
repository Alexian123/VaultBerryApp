package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricAuthManager
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricPromptRequest
import com.alexianhentiu.vaultberryapp.presentation.utils.biometric.BiometricStatus
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.AuthCredentials
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.state.BiometricState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {

    private val _biometricState = MutableStateFlow<BiometricState>(BiometricState.Idle)
    val biometricState: StateFlow<BiometricState> = _biometricState

    private val _credentialsEvent = Channel<AuthCredentials>()
    val credentialsEvent = _credentialsEvent.receiveAsFlow()

    private val _startBiometricPrompt = MutableSharedFlow<BiometricPromptRequest>()
    val startBiometricPrompt: SharedFlow<BiometricPromptRequest> =
        _startBiometricPrompt.asSharedFlow()

    private val _hasStoredCredentials = MutableStateFlow<Boolean>(false)
    val hasStoredCredentials: StateFlow<Boolean> = _hasStoredCredentials

    // Internal state to hold pending data until biometric authentication is successful
    private var pendingStoreEmail: String? = null
    private var pendingStorePassword: String? = null
    private var pendingEncryptedData: EncryptedAuthCredentials? = null

    init {
        _hasStoredCredentials.value = biometricAuthManager.hasStoredCredentials()
    }

    fun requestStoreCredentials(email: String, password: String) {
        viewModelScope.launch {
            _biometricState.value = BiometricState.Loading
            when (val result = biometricAuthManager.isBiometricAvailable()) {
                BiometricStatus.Available -> {
                    try {
                        pendingStoreEmail = email
                        pendingStorePassword = password
                        val passwordCipher = biometricAuthManager.getCipherForEncryption()
                        _startBiometricPrompt.emit(
                            BiometricPromptRequest.Store(passwordCipher)
                        )
                    } catch (e: Exception) {
                        _biometricState.value = BiometricState.Error(
                            ErrorInfo(
                                type = ErrorType.BIOMETRIC,
                                source = "BiometricViewModel",
                                message = "Failed to prepare encryption for storage: ${e.message}"
                            )
                        )
                    }
                }
                is BiometricStatus.Error -> {
                    _biometricState.value = BiometricState.Error(result.info)
                }
            }
        }
    }

    fun requestAuthenticateAndRetrieveCredentials() {
        viewModelScope.launch {
            _biometricState.value = BiometricState.Loading
            when (val result = biometricAuthManager.isBiometricAvailable()) {
                BiometricStatus.Available -> {
                    val encryptedData = biometricAuthManager.getCredentials()
                    if (encryptedData == null) {
                        _biometricState.value = BiometricState.Error(
                            ErrorInfo(
                                type = ErrorType.BIOMETRIC,
                                source = "BiometricViewModel",
                                message = "No stored credentials found to retrieve."
                            )
                        )
                        return@launch
                    }
                    try {
                        pendingEncryptedData = encryptedData
                        val passwordCipher = biometricAuthManager.getCipherForDecryption(
                            encryptedData.passwordIv
                        )
                        _startBiometricPrompt.emit(
                            BiometricPromptRequest.Retrieve(passwordCipher, encryptedData)
                        )
                    } catch (e: Exception) {
                        _biometricState.value = BiometricState.Error(
                            ErrorInfo(
                                type = ErrorType.BIOMETRIC,
                                source = "BiometricViewModel",
                                message = "Failed to prepare decryption for retrieval: ${e.message}"
                            )
                        )
                    }
                }
                is BiometricStatus.Error -> {
                    _biometricState.value = BiometricState.Error(result.info)
                }
            }
        }
    }

    fun onBiometricStoreSuccess(result: BiometricPrompt.AuthenticationResult) {
        viewModelScope.launch {
            try {
                val email = pendingStoreEmail ?: throw IllegalStateException("Email is null.")
                val password = pendingStorePassword
                    ?: throw IllegalStateException("Password is null.")

                val encryptedAuthCredentials = biometricAuthManager.performEncryption(
                    cryptoObject = result.cryptoObject,
                    password = password,
                    email = email
                )
                biometricAuthManager.storeCredentials(encryptedAuthCredentials)
                _hasStoredCredentials.value = true
                _biometricState.value = BiometricState.CredentialsStored
                clearPendingStoreData()
            } catch (e: Exception) {
                _biometricState.value = BiometricState.Error(
                    ErrorInfo(
                        type = ErrorType.BIOMETRIC,
                        source = "BiometricViewModel",
                        message = "Error storing credentials: ${e.message ?: "Unknown error"}"
                    )
                )
            }
        }
    }

    fun onBiometricRetrieveSuccess(result: BiometricPrompt.AuthenticationResult) {
        viewModelScope.launch {
            try {
                val encryptedData = pendingEncryptedData
                    ?: throw IllegalStateException("Encrypted data is null.")

                val credentials = biometricAuthManager.performDecryption(
                    cryptoObject = result.cryptoObject,
                    encryptedData = encryptedData
                )
                _biometricState.value = BiometricState.Authenticated
                _credentialsEvent.send(credentials)
                clearPendingRetrieveData()
            } catch (e: Exception) {
                _biometricState.value = BiometricState.Error(
                    ErrorInfo(
                        type = ErrorType.BIOMETRIC,
                        source = "BiometricViewModel",
                        message = "Error decrypting credentials: ${e.message ?: "Unknown error"}"
                    )
                )
            }
        }
    }

    fun onBiometricAuthError(message: String) {
        _biometricState.value = BiometricState.Error(
            ErrorInfo(
                type = ErrorType.BIOMETRIC,
                source = "BiometricViewModel",
                message = message
            )
        )
        clearPendingData()
    }

    fun clearStoredCredentials() {
        viewModelScope.launch {
            biometricAuthManager.clearStoredCredentials()
            _biometricState.value = BiometricState.ClearedCredentials
            _hasStoredCredentials.value = false
            clearPendingData()
        }
    }

    fun resetState() {
        _biometricState.value = BiometricState.Idle
        clearPendingData()
    }

    private fun clearPendingStoreData() {
        pendingStoreEmail = null
        pendingStorePassword = null
    }

    private fun clearPendingRetrieveData() {
        pendingEncryptedData = null
    }

    private fun clearPendingData() {
        clearPendingStoreData()
        clearPendingRetrieveData()
    }
}