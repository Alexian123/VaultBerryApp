package com.alexianhentiu.vaultberryapp.domain.utils

interface StringResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg args: Any): String
}