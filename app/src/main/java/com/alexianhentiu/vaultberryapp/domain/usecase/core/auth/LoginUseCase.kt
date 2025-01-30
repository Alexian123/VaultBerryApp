package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.DecryptKeyUseCase

class LoginUseCase(
    private val userRepository: UserRepository,
    private val decryptKeyUseCase: DecryptKeyUseCase
) {
    suspend operator fun invoke(loginCredentials: LoginCredentials): APIResult<DecryptedKey> {
        when (val result = userRepository.login(loginCredentials)) {
            is APIResult.Success -> {
                val keyChain = result.data
                val decryptedVaultKey = decryptKeyUseCase(
                    password = loginCredentials.password,
                    salt = keyChain.salt,
                    encryptedKey = keyChain.vaultKey
                )
                return APIResult.Success(decryptedVaultKey)
            }
            is APIResult.Error -> {
                return APIResult.Error(result.message)
            }
        }
    }
}