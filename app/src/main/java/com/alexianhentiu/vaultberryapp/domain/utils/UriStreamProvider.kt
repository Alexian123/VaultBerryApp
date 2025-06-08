package com.alexianhentiu.vaultberryapp.domain.utils

import java.io.InputStream
import java.net.URI

interface UriStreamProvider {
    fun openInputStream(uri: URI): InputStream?
}