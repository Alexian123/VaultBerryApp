package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(user: User): Boolean {
        return userRepository.register(user)
    }
}