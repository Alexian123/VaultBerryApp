package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.ui.components.bars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.AccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.miscellaneous.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.state.AccountState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AccountViewModel

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    navController: NavController
) {
    val vaultKey = navController
        .previousBackStackEntry?.savedStateHandle?.get<DecryptedKey>("vaultKey")

    val accountState by viewModel.accountState.collectAsState()
    val account by viewModel.account.collectAsState()

    when (accountState) {
        is AccountState.Init -> {
            viewModel.getAccount()
        }

        is AccountState.Loading -> {
            LoadingScreen()
        }

        is AccountState.Idle -> {
            Scaffold(
                topBar = {
                    TopBarWithBackButton(
                        onBackClick = { navController.popBackStack() },
                        title = "Account"
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    AccountForm(
                        account = account!!,
                        onSaveInfo = { firstName, lastName, email ->
                            email?.let { viewModel.changeEmail(it) }
                            viewModel.changeName(firstName, lastName)
                        },
                        onChangePassword = { newPassword ->
                            if (vaultKey != null) {
                                viewModel.changePassword(vaultKey, newPassword)
                            }
                            // TODO: Obtain the new key and send it back to the vault screen
                        },
                        onDeleteAccount = {
                            viewModel.deleteAccount()
                        },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
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