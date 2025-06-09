package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetAppInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class GetAppInfoUseCaseImpl(
    private val appInfoProvider: AppInfoProvider,
    private val stringResourceProvider: StringResourceProvider
) : GetAppInfoUseCase {
    override fun invoke(info: AppInfo): UseCaseResult<String> {
        return try {
            when (info) {
                AppInfo.VERSION_NAME -> {
                    UseCaseResult.Success(appInfoProvider.getAppVersionName())
                }

                AppInfo.APP_NAME -> {
                    UseCaseResult.Success(appInfoProvider.getAppName())
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