package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import javax.inject.Inject

class DecryptKeyUseCase @Inject constructor(
    private val handler: VaultSecurityHandler
) {
    operator fun invoke(
        password: String,
        salt: String,
        encryptedKey: String
    ): UseCaseResult<ByteArray> {
        try {
            val decryptedKey = handler.decryptKey(password, salt, encryptedKey)
            return UseCaseResult.Success(decryptedKey)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.KEY_DECRYPTION_FAILURE,
                "Vault Guardian",
                e.message ?: "Unknown error"
            )
        }
    }
}