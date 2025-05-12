package com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DebouncedBackButton(navController: NavController, enabled: Boolean = true, delayMs: Long = 200) {
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
        Icon(Icons.Filled.ArrowBackIosNew, "Back")
    }

    BackHandler(enabled = isEnabledInternal) {
        onBack()
    }
}