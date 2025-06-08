package com.alexianhentiu.vaultberryapp.utils

import com.alexianhentiu.vaultberryapp.data.security.AESHandler
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import org.junit.Before
import org.junit.Test

class AESHandlerTest {

    companion object {
        private val iv = "123456789012".toByteArray()
    }

    private lateinit var handler: GeneralCryptoHandler

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
}