package com.alexianhentiu.vaultberryapp.domain.usecase.general.account

import android.util.Log
import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.request.PasswordChangeRequest
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth.GeneratePasswordPairUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptKeyUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.GenerateKeyChainUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.ReEncryptVaultUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class ChangePasswordUseCase(
    private val accountRepository: AccountRepository,
    private val generatePasswordPairUseCase: GeneratePasswordPairUseCase,
    private val generateKeyChainUseCase: GenerateKeyChainUseCase,
    private val decryptKeyUseCase: DecryptKeyUseCase,
    private val reEncryptVaultUseCase: ReEncryptVaultUseCase
) {
    suspend operator fun invoke(
        decryptedKey: DecryptedKey,
        newPassword: String,
        reEncrypt: Boolean = false
    ): ActionResult<String> {

        val newPasswordPairResult = generatePasswordPairUseCase(newPassword)
        if (newPasswordPairResult is ActionResult.Error) {
            return newPasswordPairResult
        }
        val newPasswordPair = (newPasswordPairResult as ActionResult.Success).data

        val generateKeyChainResult = generateKeyChainUseCase(
            newPasswordPair.regularPassword,
            newPasswordPair.recoveryPassword,
            decryptedKey
        )
        if (generateKeyChainResult is ActionResult.Error) {
            return generateKeyChainResult
        }
        val newKeyChain = (generateKeyChainResult as ActionResult.Success).data

        // Re-encrypt vault if requested
        if (reEncrypt) {
            val decryptKeyResult = decryptKeyUseCase(
                newPassword,
                newKeyChain.salt,
                newKeyChain.vaultKey
            )
            if (decryptKeyResult is ActionResult.Error) {
                return decryptKeyResult
            }
            val newDecryptedKey = (decryptKeyResult as ActionResult.Success).data

            val reEncryptResult = reEncryptVaultUseCase(decryptedKey, newDecryptedKey)
            if (reEncryptResult is ActionResult.Error) {
                return reEncryptResult
            }
            val msg = (reEncryptResult as ActionResult.Success).data
            Log.d("ChangePasswordUseCase", msg.message)
        }

        val passwordChangeRequest = PasswordChangeRequest(
            passwordPair = newPasswordPair,
            keyChain = newKeyChain
        )
        return when (val changeResult = accountRepository.changePassword(passwordChangeRequest)) {
            is APIResult.Success -> {
                ActionResult.Success(newPasswordPair.recoveryPassword)
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