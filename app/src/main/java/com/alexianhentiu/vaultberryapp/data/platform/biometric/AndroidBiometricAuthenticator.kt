package com.alexianhentiu.vaultberryapp.data.platform.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.platform.crypto.AndroidSecureKeyHandler
import com.alexianhentiu.vaultberryapp.data.security.AESCipherProvider
import com.alexianhentiu.vaultberryapp.domain.common.BiometricStatus
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.AuthCredentials
import com.alexianhentiu.vaultberryapp.domain.model.CipherContext
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.domain.security.BiometricAuthenticator
import com.alexianhentiu.vaultberryapp.domain.security.CipherCache
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import java.util.UUID
import javax.crypto.Cipher

class AndroidBiometricAuthenticator(
    private val stringResourceProvider: StringResourceProvider,
    private val keyHandler: AndroidSecureKeyHandler,
    private val cipherProvider: AESCipherProvider,
    private val cipherCache: CipherCache,
    private val credentialsRepository: CredentialsRepository,
    private val context: Context
) : BiometricAuthenticator {

    companion object {
        private const val KEY_ALIAS = "BiometricCredentialsKey"
    }

    override suspend fun hasStoredCredentials(): Boolean =
        credentialsRepository.hasStoredCredentials()

    override suspend fun clearStoredCredentials() {
        credentialsRepository.clearStoredCredentials()
        keyHandler.deleteKey(KEY_ALIAS)
    }

    override fun getEncryptionContext(): CipherContext {
        val key = keyHandler.getKey(KEY_ALIAS) ?: keyHandler.generateKey(KEY_ALIAS)
        val cipher = cipherProvider.getCipherForEncryption(key)
        val token = UUID.randomUUID().toString()
        val context = CipherContext(token)
        cipherCache.setCipher(context, cipher)
        return context
    }

    override fun getDecryptionContext(iv: ByteArray): CipherContext {
        val key = keyHandler.getKey(KEY_ALIAS) ?: throw IllegalStateException(
            stringResourceProvider.getString(R.string.error_secret_key_not_found)
        )
        val cipher = cipherProvider.getCipherForDecryption(iv, key)
        val token = UUID.randomUUID().toString()
        val context = CipherContext(token)
        cipherCache.setCipher(context, cipher)
        return context
    }

    override fun performEncryption(
        context: CipherContext,
        password: String,
        email: String
    ): EncryptedAuthCredentials {
        val cipher = cipherCache.getCipher(context) as? Cipher
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

    override fun performDecryption(
        context: CipherContext,
        encryptedData: EncryptedAuthCredentials
    ): AuthCredentials {
        val cipher = cipherCache.getCipher(context) as? Cipher
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

    override suspend fun getCredentials(): EncryptedAuthCredentials? =
        credentialsRepository.getCredentials()

    override suspend fun storeCredentials(encryptedAuthCredentials: EncryptedAuthCredentials) {
        credentialsRepository.storeCredentials(encryptedAuthCredentials)
    }

    override fun isBiometricAvailable(): BiometricStatus {
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