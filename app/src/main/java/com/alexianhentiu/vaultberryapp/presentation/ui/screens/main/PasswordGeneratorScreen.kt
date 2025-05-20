package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.PasswordGeneratorForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun PasswordGeneratorScreen(
    navController: NavController
) {
    val activity = LocalActivity.current as ComponentActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = "Password Generator"
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            PasswordGeneratorForm(
                generatePassword = utilityViewModel::generatePassword,
                onPasswordCopy = utilityViewModel::copyToClipboard,
                evaluateStrength = utilityViewModel::evalPasswordStrength
            )
        }
    }
}