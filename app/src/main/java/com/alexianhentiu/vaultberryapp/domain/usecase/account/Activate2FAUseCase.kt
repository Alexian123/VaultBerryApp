package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface Activate2FAUseCase {
    suspend operator fun invoke(totpCode: String): UseCaseResult<MessageResponse>
}