package com.alexianhentiu.vaultberryapp.utils

import com.alexianhentiu.vaultberryapp.data.security.AESHandler
import org.junit.Before
import org.junit.Test

class AESHandlerTest {

    companion object {
        private val iv = "123456789012".toByteArray()
    }

    private lateinit var handler: AESHandler

    @Before
    fun initHandler() {
        handler = AESHandler()
    }

    @Test
    fun givenMode_whenGenerateKey_thenReturnNotEmptyKey() {
        assert(handler.generateKey().isNotEmpty())
    }

    @Test
    fun givenBytesAndIV_whenEncrypt_thenReturnNotEmptyBytes() {
        val key = handler.generateKey()
        val encrypted = handler.encrypt("test".toByteArray(), key, iv)
        assert(encrypted.isNotEmpty())
    }

    @Test
    fun givenBytes_whenDecrypt_thenReturnOriginalBytes() {
        val key = handler.generateKey()
        val encrypted = handler.encrypt("test".toByteArray(), key, iv)
        val decrypted = handler.decrypt(encrypted, key, iv)
        assert(String(decrypted) == "test")
    }

    @Test
    fun givenNonEmptyPassword_whenDeriveKeyFromPassword_thenReturnNotEmptyKey() {
        val key = handler.deriveKeyFromPassword("password", "salt".toByteArray())
        assert(key.isNotEmpty())
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