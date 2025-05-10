package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class DeleteAccountUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return when (val response = accountRepository.deleteAccount()) {
            is APIResult.Success -> {
                UseCaseResult.Success(response.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    response.source,
                    response.message
                )
            }
        }
    }
}