package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase

class RecoveryLoginUseCase(
    private val userRepository: UserRepository,
    private val decryptKeyUseCase: DecryptKeyUseCase
) {
    suspend operator fun invoke(
        loginCredentials: LoginCredentials,
        recoveryPassword: String
    ): APIResult<DecryptedKey> {
        when (val result = userRepository.recoveryLogin(loginCredentials)) {
            is APIResult.Success -> {
                val keyChain = result.data
                val decryptedVaultKey = decryptKeyUseCase(
                    password = recoveryPassword,
                    salt = keyChain.salt,
                    encryptedKey = keyChain.recoveryKey
                )
                return APIResult.Success(decryptedVaultKey)
            }
            is APIResult.Error -> {
                return APIResult.Error(result.message)
            }
        }
    }
}