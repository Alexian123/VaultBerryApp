package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.auth

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.request.LoginRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AuthRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.singleton.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.security.AuthGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val authGuardian: AuthGuardian,
    private val decryptKeyUseCase: DecryptKeyUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        totpCode: String? = null
    ): UseCaseResult<DecryptedKey> {
        // Initialize guardian
        authGuardian.init(email, password)

        // Get client first message
        val firstMessage = authGuardian.getClientFirstMessage()

        // Send first message to server
        val credentials1 = LoginRequest(email, firstMessage, totpCode)
        val response1 = authRepository.loginFirstStep(credentials1)
        if (response1 is APIResult.Error) {
            return UseCaseResult.Error(
                ErrorType.EXTERNAL,
                response1.source,
                response1.message
            )
        }
        val response1Data = (response1 as APIResult.Success).data

        // Get client final message
        val clientFinalMessage = authGuardian.getClientFinalMessage(response1Data.serverMessage)

        // Send client final message to server
        val credentials2 = LoginRequest(email, clientFinalMessage, totpCode)
        val response2 = authRepository.loginSecondStep(credentials2)
        if (response2 is APIResult.Error) {
            return UseCaseResult.Error(
                ErrorType.EXTERNAL,
                response2.source,
                response2.message
            )
        }
        val response2Data = (response2 as APIResult.Success).data

        // Check server final message
        try {
            authGuardian.checkServerFinalMessage(response2Data.serverMessage)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Auth Guardian",
                "Server final message verification failed: ${e.message}"
            )
        }

        // Check if keychain is null
        if (response2Data.keyChain == null) {
            return UseCaseResult.Error(
                ErrorType.INTERNAL,
                "Auth Guardian",
                "Keychain is null"
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
    }

}