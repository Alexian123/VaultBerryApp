package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.RegisterForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.RegisterState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.RegisterViewModel

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
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Registration successful!"
                        )
                        TextButton(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Go to login")
                        }
                    }
                }
            }
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