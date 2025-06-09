package com.alexianhentiu.vaultberryapp.application.usecase.auth

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.HttpResponseCode
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.security.MessageExchangeAuthClient
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class LoginUseCaseImpl(
    private val stringResourceProvider: StringResourceProvider,
    private val authRepository: AuthRepository,
    private val messageExchangeAuthClient: MessageExchangeAuthClient,
    private val decryptKeyUseCase: DecryptKeyUseCase
) : LoginUseCase {
    override suspend operator fun invoke(
        email: String,
        password: String,
        totpCode: String?
    ): UseCaseResult<ByteArray> {
        try {
            // Initialize guardian
            messageExchangeAuthClient.init(email, password)

            // Get client first message
            val firstMessage = messageExchangeAuthClient.getClientFirstMessage()

            // Send first message to server
            val credentials1 = LoginRequest(email, firstMessage, totpCode)
            val response1 = authRepository.loginFirstStep(credentials1)
            if (response1 is ApiResult.Error) {
                when (response1.code) {
                    HttpResponseCode.BAD_REQUEST.code -> { // 2FA is required
                        return UseCaseResult.Error(
                            ErrorInfo(
                                ErrorType.TWO_FACTOR_REQUIRED,
                                response1.source,
                                response1.message
                            )
                        )
                    }

                    HttpResponseCode.FORBIDDEN.code -> { // Activation is required
                        return UseCaseResult.Error(
                            ErrorInfo(
                                ErrorType.ACTIVATION_REQUIRED,
                                response1.source,
                                response1.message
                            )
                        )
                    }

                    else -> return UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            response1.source,
                            response1.message
                        )
                    )
                }
            }
            val response1Data = (response1 as ApiResult.Success).data

            // Get client final message
            val clientFinalMessage = messageExchangeAuthClient
                .getClientFinalMessage(response1Data.serverMessage)

            // Send client final message to server
            val credentials2 = LoginRequest(email, clientFinalMessage, totpCode)
            val response2 = authRepository.loginSecondStep(credentials2)
            if (response2 is ApiResult.Error) {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.API,
                        response2.source,
                        response2.message
                    )
                )
            }
            val response2Data = (response2 as ApiResult.Success).data

            // Check server final message
            try {
                messageExchangeAuthClient.checkServerFinalMessage(response2Data.serverMessage)
            } catch (e: Exception) {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.SERVER_IDENTITY_VERIFICATION_FAILURE,
                        stringResourceProvider.getString(
                            R.string.message_exchange_auth_client_error_source
                        ),
                        e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                    )
                )
            }

            // Check if keychain is null
            if (response2Data.keyChain == null) {
                return UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.MISSING_KEYCHAIN,
                        stringResourceProvider.getString(
                            R.string.message_exchange_auth_client_error_source
                        ),
                        stringResourceProvider.getString(R.string.missing_keychain)
                    )
                )
            }

            // Decrypt vault key
            val decryptedKeyResult = decryptKeyUseCase(
                password,
                response2Data.keyChain.salt,
                response2Data.keyChain.vaultKey
            )
            if (decryptedKeyResult is UseCaseResult.Error) {
                return decryptedKeyResult
            }
            val decryptedKey = (decryptedKeyResult as UseCaseResult.Success).data

            // Return decrypted key
            return UseCaseResult.Success(decryptedKey)
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