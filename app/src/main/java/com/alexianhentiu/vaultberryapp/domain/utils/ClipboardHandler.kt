package com.alexianhentiu.vaultberryapp.domain.utils

interface ClipboardHandler {
    fun copyToClipboard(text: String, label: String = "")
}