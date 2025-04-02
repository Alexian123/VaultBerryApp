package com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AESHandler : CryptographyHandler {

    private val algorithm = "AES"
    private val mode = "GCM/NoPadding"
    private val keySize = 256

    override fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(algorithm)
        keyGenerator.init(keySize)
        return keyGenerator.generateKey()
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

    override fun deriveKeyFromPassword(password: String, salt: ByteArray): SecretKey {
        if (password.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        val keySpec = PBEKeySpec(password.toCharArray(), salt, 65536, keySize)
        val keyFactory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = keyFactory.generateSecret(keySpec).encoded
        return SecretKeySpec(keyBytes, algorithm)
    }
}