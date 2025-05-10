package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.KeyChain
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GenerateKeyChainUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        password: String,
        recoveryPassword: String,
        decryptedKey: DecryptedKey?
    ): UseCaseResult<KeyChain> {
        try {
            val keyChain = vaultGuardian.generateKeyChain(password, recoveryPassword, decryptedKey)
            return UseCaseResult.Success(keyChain)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Key chain generation failed: ${e.message}"
            )
        }
    }
}