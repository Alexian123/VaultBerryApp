package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import javax.inject.Inject

class GenerateKeyChainUseCase @Inject constructor(
    private val stringResourceProvider: StringResourceProvider,
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
                stringResourceProvider.getString(R.string.vault_security_handler_error_source),
                e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }
}