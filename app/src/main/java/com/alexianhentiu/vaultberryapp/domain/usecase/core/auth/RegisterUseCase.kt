package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase
) {
    suspend operator fun invoke(account: Account, password: String): APIResult<String> {
        val recoveryPassword = generatePasswordUseCase()
        val keyChain = generateKeyChainUseCase(password, recoveryPassword)
        val user = User(account, keyChain)
        return when (val result = userRepository.register(user)) {
            is APIResult.Success -> {
                APIResult.Success(recoveryPassword)
            }

            is APIResult.Error -> {
                APIResult.Error(result.message)
            }
        }
    }
}