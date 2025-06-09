package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.internal.Extract2FASecretUseCase
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.TotpData
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Setup2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class Setup2FAUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val extract2FASecretUseCase: Extract2FASecretUseCase,
    private val base64Handler: Base64Handler,
    private val stringResourceProvider: StringResourceProvider
) : Setup2FAUseCase {

    override suspend operator fun invoke(): UseCaseResult<TotpData> {
        return try {
            when (val result = accountRepository.setup2FA()) {
                is ApiResult.Success -> {
                    val totpResponse = result.data
                    val secretResult = extract2FASecretUseCase(totpResponse.provisioningUri)
                    if (secretResult is UseCaseResult.Error) {
                        return secretResult
                    }
                    val secret = (secretResult as UseCaseResult.Success).data

                    try {
                        val qrBytes = base64Handler.decode(totpResponse.qrCode)
                        UseCaseResult.Success(
                            TotpData(
                                secret = secret,
                                qrCodeBytes = qrBytes
                            )
                        )
                    } catch (e: Exception) {
                        return UseCaseResult.Error(
                            ErrorInfo(
                                ErrorType.TWO_FACTOR_SETUP_FAILURE,
                                stringResourceProvider.getString(
                                    R.string.base64_handler_error_source
                                ),
                                e.message ?: stringResourceProvider.getString(
                                    R.string.unknown_error
                                )
                            )
                        )
                    }
                }

                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            result.source,
                            result.message
                        )
                    )
                }
            }
        } catch (e: Exception) {
            UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}