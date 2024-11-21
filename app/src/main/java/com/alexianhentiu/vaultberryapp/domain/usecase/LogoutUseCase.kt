package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Boolean {
        return userRepository.logout()
    }
}