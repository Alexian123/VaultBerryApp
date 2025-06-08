package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.domain.model.CipherContext
import com.alexianhentiu.vaultberryapp.domain.security.CipherCache
import javax.crypto.Cipher

class CipherCacheImpl : CipherCache {

    private val cipherCache = mutableMapOf<String, Cipher?>()

    override fun getCipher(context: CipherContext): Cipher? {
        return cipherCache[context.token]
    }

    override fun setCipher(
        context: CipherContext,
        cipher: Any?
    ) {
        cipherCache[context.token] = cipher as? Cipher?
    }

    override fun deleteCipher(context: CipherContext) {
        cipherCache.remove(context.token)
    }

}