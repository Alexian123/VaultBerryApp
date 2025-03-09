package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import android.net.Uri
import com.alexianhentiu.vaultberryapp.domain.model.TotpResponse
import com.alexianhentiu.vaultberryapp.domain.utils.ActionResult

class Extract2FASecret {

    operator fun invoke(totpResponse: TotpResponse): ActionResult<String> {
        val uri = Uri.parse(totpResponse.provisioningUri)
        val secret = uri.getQueryParameter("secret")
        return if (secret != null) {
            ActionResult.Success(secret)
        } else {
            ActionResult.Error("Secret not found in provisioning URI")
        }
    }
}