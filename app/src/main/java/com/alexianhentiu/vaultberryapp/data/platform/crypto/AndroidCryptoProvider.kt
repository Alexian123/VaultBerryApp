package com.alexianhentiu.vaultberryapp.data.platform.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidCryptoProvider @Inject constructor(
    private val stringResourceProvider: StringResourceProvider
) {

    companion object {
        private const val KEY_ALIAS = "BiometricCredentialsKey"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH = 16
    }

    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    fun getSecretKey(): SecretKey? {
        return try {
            keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        } catch (_: Exception) {
            null
        }
    }

    fun getCipherForEncryption(): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = getSecretKey() ?: generateSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    fun getCipherForDecryption(iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = getSecretKey() ?: throw IllegalStateException(
            stringResourceProvider.getString(R.string.error_secret_key_not_found)
        )
        val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        return cipher
    }

    fun deleteKey() {
        keyStore.deleteEntry(KEY_ALIAS)
    }
}