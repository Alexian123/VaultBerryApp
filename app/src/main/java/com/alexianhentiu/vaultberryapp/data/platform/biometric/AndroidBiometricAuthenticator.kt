package com.alexianhentiu.vaultberryapp.data.platform.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.platform.crypto.AndroidEncryptDecryptProcessor
import com.alexianhentiu.vaultberryapp.domain.common.BiometricStatus
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.AuthCredentials
import com.alexianhentiu.vaultberryapp.domain.common.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidBiometricAuthenticator @Inject constructor(
    private val stringResourceProvider: StringResourceProvider,
    private val processor: AndroidEncryptDecryptProcessor,
    private val credentialsRepository: CredentialsRepository,
    @ApplicationContext private val context: Context
) {
    fun hasStoredCredentials(): Boolean = credentialsRepository.hasStoredCredentials()

    fun clearStoredCredentials() {
        credentialsRepository.clearStoredCredentials()
        processor.deleteKey()
    }

    fun getCipherForEncryption(): Cipher {
        return processor.getCipherForEncryption()
    }

    fun getCipherForDecryption(iv: ByteArray): Cipher {
        return processor.getCipherForDecryption(iv)
    }

    fun performEncryption(
        cryptoObject: BiometricPrompt.CryptoObject?,
        password: String,
        email: String
    ): EncryptedAuthCredentials {
        val cipher = cryptoObject?.cipher
            ?: throw IllegalStateException(
                stringResourceProvider.getString(
                    R.string.error_cipher_not_available_for_encryption
                )
            )
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
            ?: throw IllegalStateException(
                stringResourceProvider.getString(
                    R.string.error_cipher_not_available_for_decryption
                )
            )
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
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.Available

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.BIOMETRIC,
                    source = stringResourceProvider.getString(R.string.biometric_error_source),
                    message = stringResourceProvider.getString(R.string.biometric_error_no_hardware)
                )
            )
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.BIOMETRIC,
                    source = stringResourceProvider.getString(R.string.biometric_error_source),
                    message = stringResourceProvider.getString(
                        R.string.biometric_error_hardware_unavailable
                    )
                )
            )
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.BIOMETRIC,
                    source = stringResourceProvider.getString(R.string.biometric_error_source),
                    message = stringResourceProvider.getString(
                        R.string.biometric_error_none_enrolled
                    )
                )
            )
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.BIOMETRIC,
                    source = stringResourceProvider.getString(R.string.biometric_error_source),
                    message = stringResourceProvider.getString(
                        R.string.biometric_error_security_update_required
                    )
                )
            )
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.BIOMETRIC,
                    source = stringResourceProvider.getString(R.string.biometric_error_source),
                    message = stringResourceProvider.getString(R.string.biometric_error_unsupported)
                )
            )
            else -> BiometricStatus.Error(
                ErrorInfo(
                    type = ErrorType.BIOMETRIC,
                    source = stringResourceProvider.getString(R.string.biometric_error_source),
                    message = stringResourceProvider.getString(R.string.biometric_error_unknown)
                )
            )
        }
    }
}