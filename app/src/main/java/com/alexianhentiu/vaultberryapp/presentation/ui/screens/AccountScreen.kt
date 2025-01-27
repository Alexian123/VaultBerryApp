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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ResetPasswordForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.UpdateAccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.AccountState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AuthViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun AccountScreen(
    vaultViewModel: VaultViewModel,
    authViewModel: AuthViewModel,
    accountViewModel: AccountViewModel,
    navController: NavController,
) {
    val accountState by accountViewModel.accountState.collectAsState()

    when (accountState) {
        is AccountState.Init -> {
            accountViewModel.getAccount()
        }

        is AccountState.Loading -> {
            LoadingScreen()
        }

        is AccountState.Idle -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    UpdateAccountForm(
                        onConfirmClicked = { email, password, firstName, lastName ->
                            accountViewModel.updateAccount(email, password, firstName, lastName)
                        },
                        inputValidator = authViewModel.inputValidator
                    )
                }
            }
        }

        is AccountState.Updated -> {
            accountViewModel.resetState()
        }

        is AccountState.Deleted -> {
            authViewModel.logout()
            accountViewModel.resetState()
            navController.navigate("login")
        }

        is AccountState.ForcedPasswordReset -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    ResetPasswordForm(
                        onConfirmClick = { newPassword ->
                            accountViewModel.updateAccount(
                                email = accountViewModel.account.value!!.email,
                                password = newPassword,
                                firstName = accountViewModel.account.value!!.firstName,
                                lastName = accountViewModel.account.value!!.lastName
                            )
                        },
                        inputValidator = vaultViewModel.inputValidator
                    )
                }
            }
        }

        is AccountState.UpdatedPassword -> {
            val decryptedKey = (accountState as AccountState.UpdatedPassword).decryptedKey
            vaultViewModel.startReEncrypting(decryptedKey)
            navController.navigate("vault")
        }

        is AccountState.Error -> {
            val errorMessage = (accountState as AccountState.Error).message
            ErrorDialog(
                onConfirm = { accountViewModel.resetState() },
                title = "Account Error",
                message = errorMessage
            )
        }
    }
}