package com.alexianhentiu.vaultberryapp.domain.usecase

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(user: User): APIResult<Unit> {
        return userRepository.register(user)
    }
}