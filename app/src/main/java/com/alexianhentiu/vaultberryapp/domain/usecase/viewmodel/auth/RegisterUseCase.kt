package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.entity.User
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val generatePasswordPairUseCase: GeneratePasswordPairUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase
) {
    suspend operator fun invoke(accountInfo: AccountInfo, regularPassword: String): UseCaseResult<String> {

        val passwordPairResult = generatePasswordPairUseCase(regularPassword)
        if (passwordPairResult is UseCaseResult.Error) {
            return passwordPairResult
        }
        val passwordPair = (passwordPairResult as UseCaseResult.Success).data

        val generateKeyChainResult = generateKeyChainUseCase(
            passwordPair.regularPassword,
            passwordPair.recoveryPassword,
            null
        )
        if (generateKeyChainResult is UseCaseResult.Error) {
            return generateKeyChainResult
        }
        val keyChain = (generateKeyChainResult as UseCaseResult.Success).data

        val user = User(accountInfo, keyChain, passwordPair)
        return when (val result = authRepository.register(user)) {
            is APIResult.Success -> {
                UseCaseResult.Success(passwordPair.recoveryPassword)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    result.source,
                    result.message
                )
            }
        }
    }
}