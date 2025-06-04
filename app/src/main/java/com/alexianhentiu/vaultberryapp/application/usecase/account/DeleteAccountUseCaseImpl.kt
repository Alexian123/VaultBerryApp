package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.usecase.account.DeleteAccountUseCase

class DeleteAccountUseCaseImpl(
    private val accountRepository: AccountRepository
) : DeleteAccountUseCase {

    override suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return when (val response = accountRepository.deleteAccount()) {
            is ApiResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is ApiResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.API,
                    response.source,
                    response.message
                )
            }
        }
    }
}