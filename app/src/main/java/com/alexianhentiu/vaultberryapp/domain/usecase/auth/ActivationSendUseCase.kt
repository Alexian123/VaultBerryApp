package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface ActivationSendUseCase {
    suspend operator fun invoke(email: String): UseCaseResult<MessageResponse>
}