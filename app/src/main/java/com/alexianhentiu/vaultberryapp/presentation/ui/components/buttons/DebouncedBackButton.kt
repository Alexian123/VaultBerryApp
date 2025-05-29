package com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DebouncedBackButton(navController: NavController, enabled: Boolean = true, delayMs: Long = 1000) {
    val coroutineScope = rememberCoroutineScope()
    var isEnabledInternal by remember { mutableStateOf(enabled) }

    val onBack = {
        if (isEnabledInternal) {
            isEnabledInternal = false
            coroutineScope.launch {
                navController.popBackStack()
                delay(delayMs)
                isEnabledInternal = true
            }
        }
    }

    IconButton(onClick = onBack, enabled = isEnabledInternal) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button_content_description)
        )
    }

    BackHandler(enabled = isEnabledInternal) {
        onBack()
    }
}