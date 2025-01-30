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
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.AccountState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AccountViewModel

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    navController: NavController
) {
    val accountState by viewModel.accountState.collectAsState()

    when (accountState) {
        is AccountState.Init -> {
            viewModel.getAccount()
        }

        is AccountState.Loading -> {
            LoadingScreen()
        }

        is AccountState.Idle -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

                }
            }
        }

        is AccountState.UpdatedEmail -> {
            viewModel.resetState()
        }

        is AccountState.UpdatedName -> {
            viewModel.resetState()
        }

        is AccountState.UpdatedPassword -> {
            viewModel.resetState()
            navController.navigate("vault")
        }

        is AccountState.Deleted -> {
            viewModel.resetState()
            navController.navigate("login")
        }

        is AccountState.Error -> {
            val errorMessage = (accountState as AccountState.Error).message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Account Error",
                message = errorMessage
            )
        }
    }
}