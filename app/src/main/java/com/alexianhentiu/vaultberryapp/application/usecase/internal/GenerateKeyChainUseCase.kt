package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import javax.inject.Inject

class GenerateKeyChainUseCase @Inject constructor(
    private val handler: VaultSecurityHandler
) {

    operator fun invoke(
        password: String,
        recoveryPassword: String,
        decryptedKey: ByteArray?
    ): UseCaseResult<KeyChain> {
        try {
            val keyChain = handler.generateKeyChain(password, recoveryPassword, decryptedKey)
            return UseCaseResult.Success(keyChain)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.KEY_GENERATION_FAILURE,
                "Vault Guardian",
                e.message ?: "Unknown error"
            )
        }
    }
}