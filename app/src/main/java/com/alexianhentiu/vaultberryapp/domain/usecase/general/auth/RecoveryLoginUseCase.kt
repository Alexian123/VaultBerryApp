package com.alexianhentiu.vaultberryapp.domain.usecase.general.auth

import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptKeyUseCase
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
//        when (val response = authRepository.recoveryLogin(loginCredentials)) {
//            is APIResult.Success -> {
//                val keyChain = response.data.keychain
//
//                val decryptKeyResult = keyChain?.let {
//                    decryptKeyUseCase(
//                        password = "",
//                        salt = it.salt,
//                        encryptedKey = keyChain.recoveryKey
//                    )
//                }
//                if (decryptKeyResult is ActionResult.Error) {
//                    return decryptKeyResult
//                }
//                val decryptedVaultKey = (decryptKeyResult as ActionResult.Success).data
//
//                return ActionResult.Success(decryptedVaultKey)
//            }
//            is APIResult.Error -> {
//                return ActionResult.Error(
//                    ErrorType.EXTERNAL,
//                    response.source,
//                    response.message
//                )
//            }
//        }
        return ActionResult.Error(
            ErrorType.INTERNAL,
            "RecoveryLoginUseCase",
            "Not implemented"
        )
    }
}