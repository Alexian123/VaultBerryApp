package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(): APIResult<Unit> {
        return userRepository.logout()
    }
}