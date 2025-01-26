package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.User
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(user: User): APIResult<String> {
        return userRepository.register(user)
    }
}