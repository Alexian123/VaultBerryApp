package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class DecryptKeyUseCase(private val vaultGuardian: VaultGuardian) {
    operator fun invoke(
        password: String,
        salt: String,
        encryptedKey: String
    ): ActionResult<DecryptedKey> {
        try {
            val keyBytes = vaultGuardian.decryptKey(password, salt, encryptedKey)
            val decryptedKey = DecryptedKey(keyBytes)
            return ActionResult.Success(decryptedKey)
        } catch (e: Exception) {
            return ActionResult.Error("Key decryption failed: ${e.message}")
        }
    }
}