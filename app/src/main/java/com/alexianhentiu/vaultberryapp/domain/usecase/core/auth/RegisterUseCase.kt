package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase
) {
    suspend operator fun invoke(account: Account, regularPassword: String): ActionResult<String> {
        val generatePasswordResult = generatePasswordUseCase()
        if (generatePasswordResult is ActionResult.Error) {
            return generatePasswordResult
        }
        val recoveryPassword = (generatePasswordResult as ActionResult.Success).data

        val generateKeyChainResult = generateKeyChainUseCase(
            regularPassword,
            recoveryPassword,
            null
        )
        if (generateKeyChainResult is ActionResult.Error) {
            return generateKeyChainResult
        }
        val keyChain = (generateKeyChainResult as ActionResult.Success).data

        val passwordPair = PasswordPair(regularPassword, recoveryPassword)

        val user = User(account, keyChain, passwordPair)
        return when (val result = userRepository.register(user)) {
            is APIResult.Success -> {
                ActionResult.Success(recoveryPassword)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}