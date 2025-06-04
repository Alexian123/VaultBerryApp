package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface Setup2FAUseCase {
    suspend operator fun invoke(): UseCaseResult<String>
}