package com.alexianhentiu.vaultberryapp.domain.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo

interface GetAppInfoUseCase {
    operator fun invoke(info: AppInfo): UseCaseResult<String>
}