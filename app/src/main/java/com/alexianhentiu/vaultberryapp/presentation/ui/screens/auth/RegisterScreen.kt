package com.alexianhentiu.vaultberryapp.presentation.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.state.RegisterState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    val registerState by viewModel.registerState.collectAsState()

    when (registerState) {
        is RegisterState.Idle -> {
            RegisterForm(
                navController = navController,
                // TODO: Implement email & password validation
                onRegisterClicked = { email, password, firstName, lastName ->
                    viewModel.register(email, password,  firstName, lastName) }
            )
        }
        is RegisterState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is RegisterState.Success -> {
            Text("Registration successful!")
        }
        is RegisterState.Error -> {
            val errorMessage = (registerState as RegisterState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}