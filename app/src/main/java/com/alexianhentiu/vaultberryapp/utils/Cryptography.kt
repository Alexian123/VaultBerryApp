package com.alexianhentiu.vaultberryapp.utils

import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Cryptography {

    companion object {

        const val algorithm = "AES"
        const val mode = "CBC/PKCS5Padding"

        fun encrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray {
            val cipher = Cipher.getInstance("$algorithm/$mode")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, algorithm), ivSpec)
            return cipher.doFinal(bytes)
        }

        fun decrypt(bytes: ByteArray, keyBytes: ByteArray, iv: ByteArray): ByteArray {
            val cipher = Cipher.getInstance("$algorithm/$mode")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(keyBytes, algorithm), ivSpec)
            return cipher.doFinal(bytes)
        }

        fun deriveKeyFromPassword(password: String, salt: ByteArray, iterationCount: Int = 65536,
                                  keyLength: Int = 256): SecretKey {
            val keySpec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)
            val keyFactory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val keyBytes = keyFactory.generateSecret(keySpec).encoded
            return SecretKeySpec(keyBytes, algorithm)
        }

        fun generateSaltFromTimestamp(): ByteArray {
            val timestamp = System.currentTimeMillis()
            val randomBytes = ByteArray(8)
            java.util.Random().nextBytes(randomBytes)
            val byteBuffer = ByteBuffer.allocate(16)
            byteBuffer.putLong(timestamp)
            byteBuffer.put(randomBytes)
            return byteBuffer.array()
        }
    }
}