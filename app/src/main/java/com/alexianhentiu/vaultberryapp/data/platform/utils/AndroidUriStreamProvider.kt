package com.alexianhentiu.vaultberryapp.data.platform.utils

import android.content.Context
import com.alexianhentiu.vaultberryapp.domain.utils.UriStreamProvider
import java.io.InputStream
import java.net.URI
import androidx.core.net.toUri

class AndroidUriStreamProvider(
    private val context: Context
) : UriStreamProvider {

    override fun openInputStream(uri: URI): InputStream? {
        return try {
            val androidUri = uri.toString().toUri()
            context.contentResolver.openInputStream(androidUri)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}