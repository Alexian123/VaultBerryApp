package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo

interface RegisterUseCase {
    suspend operator fun invoke(
        accountInfo: AccountInfo,
        regularPassword: String,
        noActivation: Boolean = false
    ): UseCaseResult<String>
}