package com.alexianhentiu.vaultberryapp.domain.utils

interface AppInfoProvider {
    fun getAppVersionName(): String
    fun getAppName(): String
}