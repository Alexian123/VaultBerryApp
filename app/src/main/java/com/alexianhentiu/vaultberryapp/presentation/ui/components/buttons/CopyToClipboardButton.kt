package com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alexianhentiu.vaultberryapp.R

@Composable
fun CopyToClipboardButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(Color.Transparent)
    ) {
        Icon(
            Icons.Filled.ContentCopy,
            stringResource(R.string.copy_password_content_description)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CopyToClipboardButtonPreview() {
    CopyToClipboardButton()
}