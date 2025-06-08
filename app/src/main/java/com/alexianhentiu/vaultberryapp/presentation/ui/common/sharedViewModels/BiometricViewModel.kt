package com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.BiometricStatus
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.AuthCredentials
import com.alexianhentiu.vaultberryapp.domain.model.CipherContext
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.security.BiometricAuthenticator
import com.alexianhentiu.vaultberryapp.domain.security.CipherCache
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import com.alexianhentiu.vaultberryapp.presentation.ui.common.BiometricPromptRequest
import com.alexianhentiu.vaultberryapp.presentation.ui.handlers.biometric.BiometricState
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
    private val stringResourceProvider: StringResourceProvider,
    private val biometricAuthenticator: BiometricAuthenticator,
    val cipherCache: CipherCache
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

    // Internal state to hold pending data until authentication is successful
    private var pendingStoreEmail: String? = null
    private var pendingStorePassword: String? = null
    private var pendingEncryptedData: EncryptedAuthCredentials? = null

    init {
        viewModelScope.launch {
            _hasStoredCredentials.value = biometricAuthenticator.hasStoredCredentials()
        }
    }

    fun requestStoreCredentials(email: String, password: String) {
        viewModelScope.launch {
            _biometricState.value = BiometricState.Loading
            when (val result = biometricAuthenticator.isBiometricAvailable()) {
                BiometricStatus.Available -> {
                    try {
                        pendingStoreEmail = email
                        pendingStorePassword = password
                        val passwordCipher = biometricAuthenticator.getEncryptionContext()
                        _startBiometricPrompt.emit(
                            BiometricPromptRequest.Store(passwordCipher)
                        )
                    } catch (e: Exception) {
                        _biometricState.value = BiometricState.Error(
                            ErrorInfo(
                                type = ErrorType.BIOMETRIC,
                                source = stringResourceProvider.getString(
                                    R.string.biometric_view_model_error_source
                                ),
                                message = stringResourceProvider.getString(
                                    R.string.failed_to_prepare_encryption_for_storage_error
                                ) + "${e.message}"
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
            when (val result = biometricAuthenticator.isBiometricAvailable()) {
                BiometricStatus.Available -> {
                    val encryptedData = biometricAuthenticator.getCredentials()
                    if (encryptedData == null) {
                        _biometricState.value = BiometricState.Error(
                            ErrorInfo(
                                type = ErrorType.BIOMETRIC,
                                source = stringResourceProvider.getString(
                                    R.string.biometric_view_model_error_source
                                ),
                                message = stringResourceProvider.getString(
                                    R.string.no_credentials_found_error
                                )
                            )
                        )
                        return@launch
                    }
                    try {
                        pendingEncryptedData = encryptedData
                        val passwordCipher = biometricAuthenticator.getDecryptionContext(
                            encryptedData.passwordIv
                        )
                        _startBiometricPrompt.emit(
                            BiometricPromptRequest.Retrieve(passwordCipher)
                        )
                    } catch (e: Exception) {
                        _biometricState.value = BiometricState.Error(
                            ErrorInfo(
                                type = ErrorType.BIOMETRIC,
                                source = stringResourceProvider.getString(
                                    R.string.biometric_view_model_error_source
                                ),
                                message = stringResourceProvider.getString(
                                    R.string.failed_to_prepare_decryption_for_retrieval_error
                                ) + "${e.message}"
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

    fun onBiometricStoreSuccess(encryptionContext: CipherContext) {
        viewModelScope.launch {
            try {
                val email = pendingStoreEmail ?: throw IllegalStateException(
                    stringResourceProvider.getString(
                        R.string.email_is_null_error
                    )
                )
                val password = pendingStorePassword
                    ?: throw IllegalStateException(
                        stringResourceProvider.getString(
                            R.string.password_is_null_error
                        )
                    )

                val encryptedAuthCredentials = biometricAuthenticator.performEncryption(
                    context = encryptionContext,
                    password = password,
                    email = email
                )
                biometricAuthenticator.storeCredentials(encryptedAuthCredentials)
                _hasStoredCredentials.value = true
                _biometricState.value = BiometricState.CredentialsStored
                clearPendingStoreData()
            } catch (e: Exception) {
                _biometricState.value = BiometricState.Error(
                    ErrorInfo(
                        type = ErrorType.BIOMETRIC,
                        source = stringResourceProvider.getString(
                            R.string.biometric_view_model_error_source
                        ),
                        message = stringResourceProvider.getString(
                            R.string.failed_to_store_credentials_error
                        ) + (e.message ?: stringResourceProvider.getString(R.string.unknown_error))
                    )
                )
            } finally {
                cipherCache.deleteCipher(encryptionContext)
            }
        }
    }

    fun onBiometricRetrieveSuccess(decryptionContext: CipherContext) {
        viewModelScope.launch {
            try {
                val encryptedData = pendingEncryptedData
                    ?: throw IllegalStateException(
                        stringResourceProvider.getString(
                            R.string.encrypted_data_is_null_error
                        )
                    )

                val credentials = biometricAuthenticator.performDecryption(
                    context = decryptionContext,
                    encryptedData = encryptedData
                )
                _biometricState.value = BiometricState.Authenticated
                _credentialsEvent.send(credentials)
                clearPendingRetrieveData()
            } catch (e: Exception) {
                _biometricState.value = BiometricState.Error(
                    ErrorInfo(
                        type = ErrorType.BIOMETRIC,
                        source = stringResourceProvider.getString(
                            R.string.biometric_view_model_error_source
                        ),
                        message = stringResourceProvider.getString(
                            R.string.failed_to_retrieve_credentials_error
                        ) + (e.message ?: stringResourceProvider.getString(R.string.unknown_error))
                    )
                )
            } finally {
                cipherCache.deleteCipher(decryptionContext)
            }
        }
    }

    fun onBiometricAuthError(message: String) {
        _biometricState.value = BiometricState.Error(
            ErrorInfo(
                type = ErrorType.BIOMETRIC,
                source = stringResourceProvider.getString(
                    R.string.biometric_view_model_error_source
                ),
                message = message
            )
        )
        clearPendingData()
    }

    fun clearStoredCredentials() {
        viewModelScope.launch {
            biometricAuthenticator.clearStoredCredentials()
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