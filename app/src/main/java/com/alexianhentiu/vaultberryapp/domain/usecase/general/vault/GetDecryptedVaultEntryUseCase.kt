package com.alexianhentiu.vaultberryapp.domain.usecase.general.vault

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.specific.vault.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GetDecryptedVaultEntryUseCase(
    private val vaultRepository: VaultRepository,
    private val decryptVaultEntryUseCase: DecryptVaultEntryUseCase
) {
    suspend operator fun invoke(
        id: Long,
        decryptedKey: DecryptedKey
    ): ActionResult<DecryptedVaultEntry> {
        val vaultEntry = vaultRepository.getVaultEntryDetails(id)
        return if (vaultEntry is APIResult.Success) {
            decryptVaultEntryUseCase(vaultEntry.data, decryptedKey)
        } else {
            val result = (vaultEntry as APIResult.Error)
            ActionResult.Error(
                ErrorType.EXTERNAL,
                result.source,
                result.message
            )
        }
    }
}