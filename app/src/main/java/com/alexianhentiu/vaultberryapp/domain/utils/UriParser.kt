package com.alexianhentiu.vaultberryapp.domain.utils

interface UriParser {
    fun getQueryParameter(uriString: String, key: String): String?
}