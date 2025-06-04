package com.alexianhentiu.vaultberryapp.data.platform.utils

import android.content.Context
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider

class AndroidAppInfoProvider(
    private val context: Context
) : AppInfoProvider {

    override fun getAppVersionName(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (_: Exception) {
            "N/A"
        }.toString()
    }

    override fun getAppName(): String {
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
            context.packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (_: Exception) {
            "N/A"
        }
    }
}