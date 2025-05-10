package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.entity.User
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.auth.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.vault.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val generatePasswordPairUseCase: GeneratePasswordPairUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase
) {
    suspend operator fun invoke(accountInfo: AccountInfo, regularPassword: String): ActionResult<String> {

        val passwordPairResult = generatePasswordPairUseCase(regularPassword)
        if (passwordPairResult is ActionResult.Error) {
            return passwordPairResult
        }
        val passwordPair = (passwordPairResult as ActionResult.Success).data

        val generateKeyChainResult = generateKeyChainUseCase(
            passwordPair.regularPassword,
            passwordPair.recoveryPassword,
            null
        )
        if (generateKeyChainResult is ActionResult.Error) {
            return generateKeyChainResult
        }
        val keyChain = (generateKeyChainResult as ActionResult.Success).data

        val user = User(accountInfo, keyChain, passwordPair)
        return when (val result = authRepository.register(user)) {
            is APIResult.Success -> {
                ActionResult.Success(passwordPair.recoveryPassword)
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