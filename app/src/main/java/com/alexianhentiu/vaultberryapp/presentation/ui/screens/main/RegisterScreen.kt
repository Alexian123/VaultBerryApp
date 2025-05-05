package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.alexianhentiu.vaultberryapp.presentation.ui.components.bars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.states.RegisterState

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navManager: NavigationManager) {
    val registerState by viewModel.registerState.collectAsState()

    when (registerState) {
        is RegisterState.Idle -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navManager.navigate(NavRoute.SETTINGS) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    RegisterForm(
                        navManager = navManager,
                        onRegisterClicked = { email, password, firstName, lastName ->
                            viewModel.register(email, password, firstName, lastName)
                        },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
        }

        is RegisterState.Loading -> {
            LoadingScreen()
        }

        is RegisterState.Success -> {
            val clipboardManager = LocalClipboardManager.current
            val recoveryPassword = (registerState as RegisterState.Success).recoveryPassword
            InfoDialog(
                title = "Account registration successful",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation. " +
                        "Make sure to write it down!",
                onDismissRequest = {
                    clipboardManager.setText(AnnotatedString(recoveryPassword))
                    viewModel.resetState()
                    navManager.navigate(NavRoute.LOGIN)
                }
            )
        }

        is RegisterState.Error -> {
            val errorMessage = (registerState as RegisterState.Error).info.message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Registration Error",
                message = errorMessage
            )
        }
    }
}