package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class DecryptKeyUseCase(private val vaultGuardian: VaultGuardian) {
    operator fun invoke(
        password: String,
        salt: String,
        encryptedKey: String
    ): UseCaseResult<DecryptedKey> {
        try {
            val decryptedKey = vaultGuardian.decryptKey(password, salt, encryptedKey)
            return UseCaseResult.Success(decryptedKey)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Key decryption failed: ${e.message}"
            )
        }
    }
}