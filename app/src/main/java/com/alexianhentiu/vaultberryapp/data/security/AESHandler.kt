package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESHandler : GeneralCryptoHandler {

    override val algorithm = "AES"
    override val mode = "GCM/NoPadding"
    override val keySizeBits = 256
    override val ivLengthBytes = 12
    override val tagLengthBits = 128

    private val secureRandom = SecureRandom()

    override fun generateKey(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(algorithm)
        keyGenerator.init(keySizeBits)
        val key = keyGenerator.generateKey()
        return key.encoded
    }

    override fun generateIV(): ByteArray {
        return ByteArray(ivLengthBytes).apply { secureRandom.nextBytes(this) }
    }

    override fun generateSalt(lengthBytes: Int): ByteArray {
        return secureRandom.generateSeed(lengthBytes)
    }

    override fun encrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("$algorithm/$mode")
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(keyBytes, algorithm),
            GCMParameterSpec(tagLengthBits, iv)
        )
        return cipher.doFinal(bytes)
    }

    override fun decrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("$algorithm/$mode")
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(keyBytes, algorithm),
            GCMParameterSpec(tagLengthBits, iv)
        )
        return cipher.doFinal(bytes)
    }
}