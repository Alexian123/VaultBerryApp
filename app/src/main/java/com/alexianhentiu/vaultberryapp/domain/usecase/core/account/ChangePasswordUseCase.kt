package com.alexianhentiu.vaultberryapp.domain.usecase.core.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.model.PasswordPair
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.extra.security.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ChangePasswordUseCase(
    private val accountRepository: AccountRepository,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase
) {
    suspend operator fun invoke(
        decryptedKey: DecryptedKey,
        newPassword: String
    ): ActionResult<String> {
        val generatePasswordResult = generatePasswordUseCase()
        if (generatePasswordResult is ActionResult.Error) {
            return generatePasswordResult
        }
        val recoveryPassword = (generatePasswordResult as ActionResult.Success).data
        val newPasswordPair = PasswordPair(newPassword, recoveryPassword)

        val generateKeyChainResult = generateKeyChainUseCase(
            newPassword,
            recoveryPassword,
            decryptedKey
        )
        if (generateKeyChainResult is ActionResult.Error) {
            return generateKeyChainResult
        }
        val newKeyChain = (generateKeyChainResult as ActionResult.Success).data

        val passwordChangeRequest = PasswordChangeRequest(
            passwordPair = newPasswordPair,
            keyChain = newKeyChain
        )
        return when (val changeResult = accountRepository.changePassword(passwordChangeRequest)) {
            is APIResult.Success -> {
                ActionResult.Success(recoveryPassword)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    changeResult.source,
                    changeResult.message
                )
            }
        }
    }
}