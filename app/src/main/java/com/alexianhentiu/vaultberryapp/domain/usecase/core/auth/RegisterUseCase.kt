package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase
) {
    suspend operator fun invoke(account: Account, password: String): ActionResult<String> {
        val generatePasswordResult = generatePasswordUseCase()
        if (generatePasswordResult is ActionResult.Error) {
            return generatePasswordResult
        }
        val recoveryPassword = (generatePasswordResult as ActionResult.Success).data

        val generateKeyChainResult = generateKeyChainUseCase(password, recoveryPassword)
        if (generateKeyChainResult is ActionResult.Error) {
            return generateKeyChainResult
        }
        val keyChain = (generateKeyChainResult as ActionResult.Success).data

        val user = User(account, keyChain, password)
        return when (val result = userRepository.register(user)) {
            is APIResult.Success -> {
                ActionResult.Success(recoveryPassword)
            }

            is APIResult.Error -> {
                ActionResult.Error(result.message)
            }
        }
    }
}