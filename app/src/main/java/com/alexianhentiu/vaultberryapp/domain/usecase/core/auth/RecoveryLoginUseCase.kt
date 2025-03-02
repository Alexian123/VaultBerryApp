package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class RecoveryLoginUseCase(
    private val userRepository: UserRepository,
    private val decryptKeyUseCase: DecryptKeyUseCase
) {
    suspend operator fun invoke(
        loginCredentials: LoginCredentials,
        recoveryPassword: String
    ): ActionResult<DecryptedKey> {
        when (val response = userRepository.recoveryLogin(loginCredentials)) {
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
                return ActionResult.Error(response.message)
            }
        }
    }
}