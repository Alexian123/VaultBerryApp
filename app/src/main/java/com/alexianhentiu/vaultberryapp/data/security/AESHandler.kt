package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AESHandler(
    private val stringResourceProvider: StringResourceProvider
) : GeneralCryptoHandler {

    private val algorithm = "AES"
    private val mode = "GCM/NoPadding"
    private val keySize = 256

    override fun generateKey(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(algorithm)
        keyGenerator.init(keySize)
        val key = keyGenerator.generateKey()
        return key.encoded
    }

    override fun encrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("$algorithm/$mode")
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(keyBytes, algorithm),
            GCMParameterSpec(128, iv)
        )
        return cipher.doFinal(bytes)
    }

    override fun decrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("$algorithm/$mode")
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(keyBytes, algorithm),
            GCMParameterSpec(128, iv)
        )
        return cipher.doFinal(bytes)
    }

    override fun deriveKeyFromPassword(password: String, salt: ByteArray): ByteArray {
        if (password.isBlank()) {
            throw IllegalArgumentException(
                stringResourceProvider.getString(R.string.error_password_empty)
            )
        }
        val keySpec = PBEKeySpec(password.toCharArray(), salt, 65536, keySize)
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = keyFactory.generateSecret(keySpec).encoded
        val secretKey = SecretKeySpec(keyBytes, algorithm)
        return secretKey.encoded
    }
}