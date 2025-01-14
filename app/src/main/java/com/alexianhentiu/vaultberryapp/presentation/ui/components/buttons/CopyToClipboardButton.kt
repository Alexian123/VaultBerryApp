package com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.alexianhentiu.vaultberryapp.R

@Composable
fun CopyToClipboardButton(
    textToCopy: String,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    IconButton(
        onClick = { clipboardManager.setText(AnnotatedString(textToCopy)) },
        modifier = modifier.background(Color.Transparent)
    ) {
        Icon(
            Icons.Filled.ContentCopy,
            stringResource(R.string.copy_password_content_description)
        )
    }
}