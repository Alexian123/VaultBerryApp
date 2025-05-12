package com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun CopyToClipboardButton(
    textToCopy: String,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current as ComponentActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    IconButton(
        onClick = { utilityViewModel.copyToClipboard(textToCopy) },
        modifier = modifier.background(Color.Transparent)
    ) {
        Icon(
            Icons.Filled.ContentCopy,
            stringResource(R.string.copy_password_content_description)
        )
    }
}