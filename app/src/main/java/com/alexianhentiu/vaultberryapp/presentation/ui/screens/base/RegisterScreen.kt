package com.alexianhentiu.vaultberryapp.presentation.ui.screens.base

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
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.extra.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.RegisterState

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    val registerState by viewModel.registerState.collectAsState()

    when (registerState) {
        is RegisterState.Idle -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    RegisterForm(
                        navController = navController,
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
                    navController.navigate("login")
                }
            )
        }

        is RegisterState.Error -> {
            val errorMessage = (registerState as RegisterState.Error).message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Registration Error",
                message = errorMessage
            )
        }
    }
}