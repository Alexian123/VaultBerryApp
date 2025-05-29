package com.alexianhentiu.vaultberryapp.presentation.utils.helper

import android.content.Context

fun getAppVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (_: Exception) {
        "N/A"
    }.toString()
}

fun getAppName(context: Context): String {
    return try {
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
        context.packageManager.getApplicationLabel(applicationInfo).toString()
    } catch (_: Exception) {
        "N/A"
    }
}