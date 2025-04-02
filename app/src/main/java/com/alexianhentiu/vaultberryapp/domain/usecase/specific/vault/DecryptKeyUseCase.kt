package com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class DecryptKeyUseCase(private val vaultGuardian: VaultGuardian) {
    operator fun invoke(
        password: String,
        salt: String,
        encryptedKey: String
    ): ActionResult<DecryptedKey> {
        try {
            val decryptedKey = vaultGuardian.decryptKey(password, salt, encryptedKey)
            return ActionResult.Success(decryptedKey)
        } catch (e: Exception) {
            return ActionResult.Error(
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Key decryption failed: ${e.message}"
            )
        }
    }
}