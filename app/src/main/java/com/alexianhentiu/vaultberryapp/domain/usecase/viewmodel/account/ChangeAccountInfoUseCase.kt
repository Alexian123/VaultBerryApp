package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ChangeAccountInfoUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(accountInfo: AccountInfo): UseCaseResult<MessageResponse> {
        return when (val updateResult = accountRepository.updateAccount(accountInfo)) {
            is APIResult.Success -> {
                UseCaseResult.Success(updateResult.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    updateResult.source,
                    updateResult.message
                )
            }
        }
    }
}