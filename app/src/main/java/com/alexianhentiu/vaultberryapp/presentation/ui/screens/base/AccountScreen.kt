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
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.ui.components.bars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.AccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.extra.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.AccountState

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    navController: NavController
) {
    val vaultKey = navController
        .previousBackStackEntry?.savedStateHandle?.get<DecryptedKey>("vaultKey")

    val accountState by viewModel.accountState.collectAsState()

    val clipboardManager = LocalClipboardManager.current

    when (accountState) {
        is AccountState.Init -> {
            viewModel.getAccountInfo()
        }

        is AccountState.Loading -> {
            LoadingScreen()
        }

        is AccountState.Idle -> {
            val state = accountState as AccountState.Idle
            val account = state.account
            val is2FAEnabled = state.is2FAEnabled

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
                        account = account,
                        is2FAEnabled = is2FAEnabled,
                        onSaveInfo = { firstName, lastName, email ->
                            email?.let { viewModel.changeEmail(it) }
                            viewModel.changeName(firstName, lastName)
                            viewModel.resetState()
                        },
                        onChangePassword = { newPassword ->
                            if (vaultKey != null) {
                                viewModel.changePassword(vaultKey, newPassword)
                            }
                        },
                        onEnable2FA = {
                            viewModel.setup2FA()
                        },
                        onDisable2FA = {
                            viewModel.disable2FA()
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

        is AccountState.ChangedPassword -> {
            val recoveryPassword = (accountState as AccountState.ChangedPassword).recoveryPassword
            InfoDialog(
                title = "Password changed successfully",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    clipboardManager.setText(AnnotatedString(recoveryPassword))
                    viewModel.resetState()
                }
            )
        }

        is AccountState.Setup2FA -> {
            val secretKey = (accountState as AccountState.Setup2FA).secretKey
            InfoDialog(
                title = "2FA Setup",
                message = "Your secret key is: \"$secretKey\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    clipboardManager.setText(AnnotatedString(secretKey))
                    viewModel.resetState()
                }
            )
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