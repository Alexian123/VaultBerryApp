package com.alexianhentiu.vaultberryapp.domain.usecase.vault

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface DeleteEntryUseCase {
    suspend operator fun invoke(id: Long): UseCaseResult<MessageResponse>
}