package com.alexianhentiu.vaultberryapp.domain.usecase.core.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class GetRecoveryOTPUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String): APIResult<String> {
        return userRepository.getRecoveryOTP(email)
    }
}