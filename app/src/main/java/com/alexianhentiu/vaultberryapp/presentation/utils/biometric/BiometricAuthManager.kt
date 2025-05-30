package com.alexianhentiu.vaultberryapp.presentation.utils.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.AuthCredentials
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository

class BiometricAuthManager(
    private val cryptoManager: BiometricCryptoManager,
    private val credentialsRepository: CredentialsRepository
) {
    fun hasStoredCredentials(): Boolean = credentialsRepository.hasStoredCredentials()

    fun clearStoredCredentials() {
        credentialsRepository.clearStoredCredentials()
        cryptoManager.deleteKey()
    }

    fun storeCredentialsWithBiometric(
        activity: FragmentActivity,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (ErrorInfo) -> Unit
    ) {
        try {
            val passwordCipher = cryptoManager.getCipherForEncryption()

            val biometricPrompt = createBiometricPrompt(
                activity = activity,
                onAuthSuccess = { result ->
                    try {
                        val encryptedPassword = passwordCipher.doFinal(password.toByteArray())
                        val passwordIv = passwordCipher.iv
                        val encryptedCredentials = EncryptedAuthCredentials(
                            encryptedPassword = encryptedPassword,
                            passwordIv = passwordIv,
                            email = email
                        )
                        credentialsRepository.storeCredentials(encryptedCredentials)
                        onSuccess()
                    } catch (e: Exception) {
                        onError(
                            ErrorInfo(
                                type = ErrorType.INTERNAL,
                                source = "Biometric",
                                message = "Encryption failed: ${e.message ?: "Unknown error"}"
                            )
                        )
                    }
                },
                onAuthError = onError
            )

            val promptInfo = createPromptInfo(
                title = "Store Credentials",
                subtitle = "Authenticate to securely store your login credentials"
            )

            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(passwordCipher))

        } catch (e: Exception) {
            onError(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "Failed to initialize encryption: ${e.message}"
                )
            )
        }
    }

    fun retrieveCredentialsWithBiometric(
        activity: FragmentActivity,
        onSuccess: (AuthCredentials) -> Unit,
        onError: (ErrorInfo) -> Unit
    ) {
        val encryptedData = credentialsRepository.getCredentials()
        if (encryptedData == null) {
            onError(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "No stored credentials found"
                )
            )
            return
        }

        try {
            val passwordCipher = cryptoManager.getCipherForDecryption(encryptedData.passwordIv)

            val biometricPrompt = createBiometricPrompt(
                activity = activity,
                onAuthSuccess = { result ->
                    try {
                        val decryptedPassword = passwordCipher.doFinal(encryptedData.encryptedPassword)

                        val credentials = AuthCredentials(
                            email = encryptedData.email,
                            password = String(decryptedPassword)
                        )
                        onSuccess(credentials)
                    } catch (e: Exception) {
                        onError(
                            ErrorInfo(
                                type = ErrorType.INTERNAL,
                                source = "Biometric",
                                message = "Decryption failed: ${e.message}"
                            )
                        )
                    }
                },
                onAuthError = onError
            )

            val promptInfo = createPromptInfo(
                title = "Unlock Credentials",
                subtitle = "Authenticate to retrieve your stored login credentials"
            )

            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(passwordCipher))

        } catch (e: Exception) {
            onError(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "Failed to initialize decryption: ${e.message}"
                )
            )
        }
    }

    private fun createBiometricPrompt(
        activity: FragmentActivity,
        onAuthSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onAuthError: (ErrorInfo) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        return BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onAuthError(
                    ErrorInfo(
                        type = ErrorType.INTERNAL,
                        source = "Biometric",
                        message = errString.toString()
                    )
                )
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthSuccess(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onAuthError(
                    ErrorInfo(
                        type = ErrorType.INTERNAL,
                        source = "Biometric",
                        message = "Authentication failed"
                    )
                )
            }
        })
    }

    fun isBiometricAvailable(activity: FragmentActivity): BiometricStatus {
        val biometricManager = BiometricManager.from(activity)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.Available

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "No biometric hardware available"
                )
            )

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "Biometric hardware unavailable"
                )
            )

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "No biometrics enrolled."
                )
            )

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "Security update required"
                )
            )

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "Biometric authentication not supported"
                )
            )

            else -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "Biometric",
                    message = "Unknown biometric status"
                )
            )
        }
    }

    private fun createPromptInfo(title: String, subtitle: String): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Cancel")
            .build()
    }
}