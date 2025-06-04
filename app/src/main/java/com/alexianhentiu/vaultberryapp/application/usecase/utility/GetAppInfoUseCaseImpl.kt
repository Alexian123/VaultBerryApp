package com.alexianhentiu.vaultberryapp.application.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetAppInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider

class GetAppInfoUseCaseImpl(
    private val appInfoProvider: AppInfoProvider
) : GetAppInfoUseCase {
    override fun invoke(info: AppInfo): UseCaseResult<String> {
        return when (info) {
            AppInfo.VERSION_NAME -> {
                UseCaseResult.Success(appInfoProvider.getAppVersionName())
            }

            AppInfo.APP_NAME -> {
                UseCaseResult.Success(appInfoProvider.getAppName())
            }
        }
    }
}