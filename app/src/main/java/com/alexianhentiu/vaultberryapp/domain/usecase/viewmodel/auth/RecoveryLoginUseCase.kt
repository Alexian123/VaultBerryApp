package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.request.RecoveryLoginRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class RecoveryLoginUseCase(
    private val authRepository: AuthRepository,
    private val decryptKeyUseCase: DecryptKeyUseCase
) {
    suspend operator fun invoke(
        email: String,
        recoveryPassword: String,
        otp: String
    ): ActionResult<DecryptedKey> {
        val credentials = RecoveryLoginRequest(
            email = email,
            recoveryPassword = recoveryPassword,
            otp = otp
        )
        when (val response = authRepository.recoveryLogin(credentials)) {
            is APIResult.Success -> {
                val keyChain = response.data

                val decryptKeyResult = decryptKeyUseCase(
                    password = recoveryPassword,
                    salt = keyChain.salt,
                    encryptedKey = keyChain.recoveryKey
                )
                if (decryptKeyResult is ActionResult.Error) {
                    return decryptKeyResult
                }
                val decryptedVaultKey = (decryptKeyResult as ActionResult.Success).data

                return ActionResult.Success(decryptedVaultKey)
            }
            is APIResult.Error -> {
                return ActionResult.Error(
                    ErrorType.EXTERNAL,
                    response.source,
                    response.message
                )
            }
        }
    }
}