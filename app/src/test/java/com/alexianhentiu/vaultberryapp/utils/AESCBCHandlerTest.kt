package com.alexianhentiu.vaultberryapp.utils

import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.AESHandler
import org.junit.Before
import org.junit.Test
import javax.crypto.SecretKey

class AESCBCHandlerTest {

    companion object {
        private const val mode = "CBC/PKCS5Padding"
        private val iv = "1234567890123456".toByteArray()
    }

    private lateinit var handler: AESHandler

    @Before
    fun initHandler() {
        handler = AESHandler(mode)
    }

    @Test
    fun givenMode_whenGenerateKey_thenReturnNotEmptyKey() {
        assert(handler.generateKey().encoded.isNotEmpty())
    }

    @Test
    fun givenBytesAndIV_whenEncrypt_thenReturnNotEmptyBytes() {
        val key: SecretKey = handler.generateKey()
        val encrypted = handler.encrypt("test".toByteArray(), key.encoded, iv)
        assert(encrypted.isNotEmpty())
    }

    @Test
    fun givenBytes_whenDecrypt_thenReturnOriginalBytes() {
        val key: SecretKey = handler.generateKey()
        val encrypted = handler.encrypt("test".toByteArray(), key.encoded, iv)
        val decrypted = handler.decrypt(encrypted, key.encoded, iv)
        assert(String(decrypted) == "test")
    }

    @Test
    fun givenNonEmptyPassword_whenDeriveKeyFromPassword_thenReturnNotEmptyKey() {
        val key = handler.deriveKeyFromPassword("password", "salt".toByteArray())
        assert(key.encoded.isNotEmpty())
    }

    @Test
    fun givenEmptyPassword_whenDeriveKeyFromPassword_thenThrowIllegalArgumentException() {
        try {
            handler.deriveKeyFromPassword("", "salt".toByteArray())
        } catch (e: IllegalArgumentException) {
            assert(e.message == "Password cannot be empty")
        }
    }
}