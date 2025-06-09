package com.alexianhentiu.vaultberryapp.application.usecase.internal

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import com.alexianhentiu.vaultberryapp.domain.utils.UriParser
import javax.inject.Inject

class Extract2FASecretUseCase @Inject constructor(
    private val stringResourceProvider: StringResourceProvider,
    private val uriParser: UriParser
) {

    operator fun invoke(provisioningUri: String): UseCaseResult<String> {
        try {
            val secret = uriParser.getQueryParameter(provisioningUri, "secret")
            return if (secret != null) {
                UseCaseResult.Success(secret)
            } else {
                UseCaseResult.Error(
                    ErrorInfo(
                        ErrorType.TWO_FACTOR_SETUP_FAILURE,
                        stringResourceProvider.getString(R.string.uri_parser_error_source),
                        stringResourceProvider.getString(R.string.secret_not_found_in_uri_error)
                    )
                )
            }
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.TWO_FACTOR_SETUP_FAILURE,
                    stringResourceProvider.getString(R.string.uri_parser_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}