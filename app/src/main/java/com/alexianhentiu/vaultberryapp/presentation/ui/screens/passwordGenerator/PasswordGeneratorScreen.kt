package com.alexianhentiu.vaultberryapp.presentation.ui.screens.passwordGenerator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.PasswordGeneratorForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel

@Composable
fun PasswordGeneratorScreen(
    navController: NavController,
    utilityViewModel: UtilityViewModel
) {
    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = stringResource(R.string.password_generator_screen_title)
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