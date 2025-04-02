package com.alexianhentiu.vaultberryapp.domain.usecase.specific.auth

import com.alexianhentiu.vaultberryapp.domain.model.response.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType
import androidx.core.net.toUri

class Extract2FASecret {

    operator fun invoke(totpResponse: TotpResponse): ActionResult<String> {
        val uri = totpResponse.provisioningUri.toUri()
        val secret = uri.getQueryParameter("secret")
        return if (secret != null) {
            ActionResult.Success(secret)
        } else {
            ActionResult.Error(
                ErrorType.INTERNAL,
                "Provisioning URI",
                "Secret not found in provisioning URI"
            )
        }
    }
}