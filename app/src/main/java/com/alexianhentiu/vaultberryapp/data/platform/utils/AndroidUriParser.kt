package com.alexianhentiu.vaultberryapp.data.platform.utils

import androidx.core.net.toUri
import com.alexianhentiu.vaultberryapp.domain.utils.UriParser

class AndroidUriParser : UriParser {
    override fun getQueryParameter(uriString: String, key: String): String? {
        val uri = uriString.toUri()
        return uri.getQueryParameter(key)
    }
}