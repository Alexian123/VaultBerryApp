package com.alexianhentiu.vaultberryapp.data.platform.utils

import android.util.Base64
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler

class AndroidBase64Handler : Base64Handler {
    override fun encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun decode(encoded: String): ByteArray {
        return Base64.decode(encoded, Base64.DEFAULT)
    }
}