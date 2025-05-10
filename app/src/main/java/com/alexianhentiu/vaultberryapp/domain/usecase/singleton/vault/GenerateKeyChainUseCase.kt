package com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.KeyChain
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GenerateKeyChainUseCase(private val vaultGuardian: VaultGuardian) {

    operator fun invoke(
        password: String,
        recoveryPassword: String,
        decryptedKey: DecryptedKey?
    ): ActionResult<KeyChain> {
        try {
            val keyChain = vaultGuardian.generateKeyChain(password, recoveryPassword, decryptedKey)
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