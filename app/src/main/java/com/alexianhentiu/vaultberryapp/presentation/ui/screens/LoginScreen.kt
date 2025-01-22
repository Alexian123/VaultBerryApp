package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.AuthState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {
    val loginState by viewModel.authState.collectAsState()

    when (loginState) {
        is AuthState.LoggedOut -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    LoginForm(
                        navController = navController,
                        onLoginClicked = { email, password -> viewModel.login(email, password) },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
        }
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.LoggedIn -> {
            val vaultKey = (loginState as AuthState.LoggedIn).decryptedVaultKey
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "vaultKey",
                value = vaultKey
            )
            navController.navigate("vault")
        }
        is AuthState.Error -> {
            val errorMessage = (loginState as AuthState.Error).message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Login Error",
                message = errorMessage
            )
        }
    }
}