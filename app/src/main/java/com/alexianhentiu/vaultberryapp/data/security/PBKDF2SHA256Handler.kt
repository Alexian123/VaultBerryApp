package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.security.KeyDerivationHandler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PBKDF2SHA256Handler(
    private val stringResourceProvider: StringResourceProvider
) : KeyDerivationHandler {

    override val keySizeBits = 256
    override val kdfAlgorithm = "PBKDF2WithHmacSHA256"
    override val iterationCount = 65536

    override fun deriveKeyFromPassword(password: String, salt: ByteArray): ByteArray {
        if (password.isBlank()) {
            throw IllegalArgumentException(
                stringResourceProvider.getString(R.string.error_password_empty)
            )
        }
        val keySpec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keySizeBits)
        val keyFactory = SecretKeyFactory.getInstance(kdfAlgorithm)
        return keyFactory.generateSecret(keySpec).encoded
    }
}