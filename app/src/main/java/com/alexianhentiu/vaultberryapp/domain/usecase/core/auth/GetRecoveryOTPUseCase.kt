package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class GetRecoveryOTPUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String): ActionResult<MessageResponse> {
        return when (val response = userRepository.getRecoveryOTP(email)) {
            is APIResult.Success -> {
                ActionResult.Success(response.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(response.message)
            }
        }
    }
}