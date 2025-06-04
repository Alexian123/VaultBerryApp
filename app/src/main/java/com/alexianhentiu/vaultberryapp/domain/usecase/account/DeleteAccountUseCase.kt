package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface DeleteAccountUseCase {
    suspend operator fun invoke(): UseCaseResult<MessageResponse>
}