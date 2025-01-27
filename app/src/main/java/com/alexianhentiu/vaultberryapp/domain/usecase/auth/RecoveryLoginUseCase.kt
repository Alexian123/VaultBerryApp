package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class RecoveryLoginUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(loginCredentials: LoginCredentials): APIResult<String> {
        return userRepository.recoveryLogin(loginCredentials)
    }
}