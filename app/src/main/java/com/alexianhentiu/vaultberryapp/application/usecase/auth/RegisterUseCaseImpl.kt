package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class RegisterUseCaseImpl(
    private val authRepository: AuthRepository,
    private val generatePasswordPairUseCase: GeneratePasswordPairUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase,
    private val stringResourceProvider: StringResourceProvider
) : RegisterUseCase {
    override suspend operator fun invoke(
        accountInfo: AccountInfo,
        regularPassword: String,
        noActivation: Boolean
    ): UseCaseResult<String> {
        try {
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

            val user = User(accountInfo, keyChain, passwordPair, noActivation)
            return when (val result = authRepository.register(user)) {
                is ApiResult.Success -> {
                    UseCaseResult.Success(passwordPair.recoveryPassword)
                }

                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            result.source,
                            result.message
                        )
                    )
                }
            }
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}