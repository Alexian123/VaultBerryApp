package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo

interface GetAccountInfoUseCase {
    suspend operator fun invoke(): UseCaseResult<AccountInfo>
}