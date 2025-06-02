package com.alexianhentiu.vaultberryapp.domain.usecase.singleton

import androidx.core.net.toUri
import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class Extract2FASecret {

    operator fun invoke(totpResponse: TotpResponse): UseCaseResult<String> {
        val uri = totpResponse.provisioningUri.toUri()
        val secret = uri.getQueryParameter("secret")
        return if (secret != null) {
            UseCaseResult.Success(secret)
        } else {
            UseCaseResult.Error(
                ErrorType.TWO_FACTOR_SETUP_FAILURE,
                "Two-Factor Setup",
                "Secret not found in provisioning URI"
            )
        }
    }
}