package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.data.api.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.model.LoginCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(loginCredentials: LoginCredentials): APIResult<KeyChain> {
        return userRepository.login(loginCredentials)
    }
}