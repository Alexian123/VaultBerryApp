package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.TotpData

interface Setup2FAUseCase {
    suspend operator fun invoke(): UseCaseResult<TotpData>
}