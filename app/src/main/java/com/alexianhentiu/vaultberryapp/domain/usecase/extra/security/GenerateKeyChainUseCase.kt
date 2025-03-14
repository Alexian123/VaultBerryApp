package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GenerateKeyChainUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(password: String, recoveryPassword: String): ActionResult<KeyChain> {
        try {
            val keyChain = vaultGuardian.generateKeyChain(password, recoveryPassword)
            return ActionResult.Success(keyChain)
        } catch (e: Exception) {
            return ActionResult.Error(
                ErrorType.INTERNAL,
                "Vault Guardian",
                "Key chain generation failed: ${e.message}"
            )
        }
    }
}