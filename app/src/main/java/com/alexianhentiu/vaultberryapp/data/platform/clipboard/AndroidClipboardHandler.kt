package com.alexianhentiu.vaultberryapp.data.platform.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler

class AndroidClipboardHandler(
    private val clipboardManager: ClipboardManager
) : ClipboardHandler {

    override fun copyToClipboard(text: String, label: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}