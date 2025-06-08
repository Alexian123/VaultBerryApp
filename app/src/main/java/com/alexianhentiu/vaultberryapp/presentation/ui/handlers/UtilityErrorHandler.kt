package com.alexianhentiu.vaultberryapp.presentation.ui.handlers

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UtilityErrorHandler(
    utilityViewModel: UtilityViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        utilityViewModel.errorInfo.collectLatest { errorInfo ->
            Toast.makeText(context, errorInfo.message, Toast.LENGTH_LONG).show()
        }
    }
}