package com.alexianhentiu.vaultberryapp.domain.security

import com.alexianhentiu.vaultberryapp.domain.model.CipherContext

interface CipherCache {

    fun getCipher(context: CipherContext): Any?

    fun setCipher(context: CipherContext, cipher: Any?)

    fun deleteCipher(context: CipherContext)
}