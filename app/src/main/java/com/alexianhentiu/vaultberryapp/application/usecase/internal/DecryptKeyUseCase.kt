package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import javax.inject.Inject

class DecryptKeyUseCase @Inject constructor(
    private val stringResourceProvider: StringResourceProvider,
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
                ErrorInfo(
                    ErrorType.KEY_DECRYPTION_FAILURE,
                    stringResourceProvider.getString(R.string.vault_security_handler_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}