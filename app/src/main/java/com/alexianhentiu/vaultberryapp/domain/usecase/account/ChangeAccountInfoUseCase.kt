package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse

interface ChangeAccountInfoUseCase {
    suspend operator fun invoke(
        accountInfo: AccountInfo,
        noActivation: Boolean = false
    ): UseCaseResult<MessageResponse>
}