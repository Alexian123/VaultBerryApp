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
    suspend operator fun invoke(email: String, recoveryPassword: String): APIResult<DecryptedKey> {
       when (val result = userRepository.getRecoveryKey(email)) {
           is APIResult.Success -> {
                val recoveryKey = result.data
                val loginCredentials = LoginCredentials(
                    email = email,
                    password = recoveryKey.oneTimePassword
                )
                when (val loginResult = userRepository.recoveryLogin(loginCredentials)) {
                    is APIResult.Success -> {
                        val decryptedVaultKey = decryptKeyUseCase(
                            password = recoveryPassword,
                            salt = recoveryKey.salt,
                            encryptedKey = recoveryKey.key
                        )
                        return APIResult.Success(decryptedVaultKey)
                    }

                    is APIResult.Error -> {
                        return APIResult.Error(loginResult.message)
                    }
                }
           }

           is APIResult.Error -> {
               return APIResult.Error(result.message)
           }
       }
    }
}