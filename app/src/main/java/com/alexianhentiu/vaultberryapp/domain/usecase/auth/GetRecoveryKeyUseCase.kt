package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.RecoveryKey
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class GetRecoveryKeyUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String): APIResult<RecoveryKey> {
        return userRepository.getRecoveryKey(email)
    }
}