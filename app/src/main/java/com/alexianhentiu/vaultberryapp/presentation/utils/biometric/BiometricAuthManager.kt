package com.alexianhentiu.vaultberryapp.presentation.utils.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.AuthCredentials
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import javax.crypto.Cipher

class BiometricAuthManager(
    private val cryptoManager: BiometricCryptoManager,
    private val credentialsRepository: CredentialsRepository,
    private val applicationContext: Context
) {

    fun hasStoredCredentials(): Boolean = credentialsRepository.hasStoredCredentials()

    fun clearStoredCredentials() {
        credentialsRepository.clearStoredCredentials()
        cryptoManager.deleteKey()
    }

    fun getCipherForEncryption(): Cipher {
        return cryptoManager.getCipherForEncryption()
    }

    fun getCipherForDecryption(iv: ByteArray): Cipher {
        return cryptoManager.getCipherForDecryption(iv)
    }

    fun performEncryption(
        cryptoObject: BiometricPrompt.CryptoObject?,
        password: String,
        email: String
    ): EncryptedAuthCredentials {
        val cipher = cryptoObject?.cipher
            ?:throw IllegalStateException("Cipher not available from crypto object for encryption")
        val encryptedPassword = cipher.doFinal(password.toByteArray())
        val passwordIv = cipher.iv
        return EncryptedAuthCredentials(
            encryptedPassword = encryptedPassword,
            passwordIv = passwordIv,
            email = email
        )
    }

    fun performDecryption(
        cryptoObject: BiometricPrompt.CryptoObject?,
        encryptedData: EncryptedAuthCredentials
    ): AuthCredentials {
        val cipher = cryptoObject?.cipher
            ?: throw IllegalStateException("Cipher not available from crypto object for decryption")
        val decryptedPassword = cipher.doFinal(encryptedData.encryptedPassword)
        return AuthCredentials(
            email = encryptedData.email,
            password = String(decryptedPassword)
        )
    }

    fun getCredentials(): EncryptedAuthCredentials? = credentialsRepository.getCredentials()

    fun storeCredentials(encryptedAuthCredentials: EncryptedAuthCredentials) {
        credentialsRepository.storeCredentials(encryptedAuthCredentials)
    }

    fun isBiometricAvailable(): BiometricStatus {
        val biometricManager = BiometricManager.from(applicationContext)
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.Available

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "BiometricAuthManager",
                    message = "No biometric hardware available on this device."
                )
            )
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "BiometricAuthManager",
                    message = "Biometric hardware is currently unavailable or busy."
                )
            )
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "BiometricAuthManager",
                    message = "No biometrics (fingerprint/face) enrolled."
                )
            )
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "BiometricAuthManager",
                    message = "A security update is required for biometric authentication."
                )
            )
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.INTERNAL,
                    source = "BiometricAuthManager",
                    message = "The biometric authentication method is not supported on this device."
                )
            )
            else -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.UNKNOWN,
                    source = "BiometricAuthManager",
                    message = "An unknown biometric status occurred."
                )
            )
        }
    }
}