package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.request.RecoveryLoginRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RecoveryLoginUseCase

class RecoveryLoginUseCaseImpl(
    private val authRepository: AuthRepository,
    private val decryptKeyUseCase: DecryptKeyUseCase
) : RecoveryLoginUseCase {
    override suspend operator fun invoke(
        email: String,
        recoveryPassword: String,
        otp: String
    ): UseCaseResult<ByteArray> {
        val credentials = RecoveryLoginRequest(
            email = email,
            recoveryPassword = recoveryPassword,
            otp = otp
        )
        when (val response = authRepository.recoveryLogin(credentials)) {
            is ApiResult.Success -> {
                val keyChain = response.data

                val decryptKeyResult = decryptKeyUseCase(
                    password = recoveryPassword,
                    salt = keyChain.salt,
                    encryptedKey = keyChain.recoveryKey
                )
                if (decryptKeyResult is UseCaseResult.Error) {
                    return decryptKeyResult
                }
                val decryptedVaultKey = (decryptKeyResult as UseCaseResult.Success).data

                return UseCaseResult.Success(decryptedVaultKey)
            }
            is ApiResult.Error -> {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        response.source,
                        response.message
                    )
                )
            }
        }
    }
}