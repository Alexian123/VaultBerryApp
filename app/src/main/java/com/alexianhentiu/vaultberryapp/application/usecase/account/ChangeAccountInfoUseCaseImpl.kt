package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangeAccountInfoUseCase

class ChangeAccountInfoUseCaseImpl(
    private val accountRepository: AccountRepository
) : ChangeAccountInfoUseCase {
    override suspend operator fun invoke(
        accountInfo: AccountInfo,
        noActivation: Boolean
    ): UseCaseResult<MessageResponse> {
        return when (val updateResult = accountRepository.updateAccount(accountInfo, noActivation)) {
            is ApiResult.Success -> {
                UseCaseResult.Success(updateResult.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.API,
                    updateResult.source,
                    updateResult.message
                )
            }
        }
    }
}