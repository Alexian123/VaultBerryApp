package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.LoginState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.LoggedOut -> {
            LoginForm(
                navController = navController,
                onLoginClicked = { email, password -> viewModel.login(email, password) },
                inputValidator = viewModel.inputValidator
            )
        }
        is LoginState.Loading -> {
            LoadingScreen()
        }
        is LoginState.LoggedIn -> {
            val vaultKey = (loginState as LoginState.LoggedIn).decryptedVaultKey
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "vaultKey",
                value = vaultKey
            )
            navController.navigate("vault")
        }
        is LoginState.Error -> {
            val errorMessage = (loginState as LoginState.Error).message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Login Error",
                message = errorMessage
            )
        }
    }
}