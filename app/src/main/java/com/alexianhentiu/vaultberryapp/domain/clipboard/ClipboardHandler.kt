package com.alexianhentiu.vaultberryapp.domain.clipboard

interface ClipboardHandler {
    fun copyToClipboard(text: String, label: String = "")
}