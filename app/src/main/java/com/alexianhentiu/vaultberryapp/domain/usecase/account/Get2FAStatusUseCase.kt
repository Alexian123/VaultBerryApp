package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface Get2FAStatusUseCase {
    suspend operator fun invoke(): UseCaseResult<Boolean>
}