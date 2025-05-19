package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.AccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AccountScreenState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel

@Composable
fun AccountScreen(
    navController: NavHostController
) {
    val activity = LocalActivity.current as ComponentActivity
    val sessionViewModel: SessionViewModel = hiltViewModel(activity)
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val accountViewModel: AccountViewModel = hiltViewModel()
    val screenState by accountViewModel.accountScreenState.collectAsState()

    when (screenState) {
        is AccountScreenState.Init -> {
            accountViewModel.getAccountInfo()
        }

        is AccountScreenState.Loading -> {
            LoadingScreen()
        }

        is AccountScreenState.Idle -> {
            val account = accountViewModel.accountInfo.collectAsState()
            val is2FAEnabled = accountViewModel.is2FAEnabled.collectAsState()
            val vaultKey = sessionViewModel.decryptedKey.collectAsState()

            Scaffold(
                topBar = {
                    TopBarWithBackButton(
                        navController = navController,
                        title = "Account"
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)) {
                    AccountForm(
                        accountInfo = account.value,
                        is2FAEnabled = is2FAEnabled.value,
                        onSaveInfo = { email, firstName, lastName ->
                            accountViewModel.changeAccountInfo(email, firstName, lastName)
                        },
                        onChangePassword = { newPassword, reEncrypt ->
                            accountViewModel.changePassword(vaultKey.value, newPassword, reEncrypt)
                        },
                        onEnable2FA = {
                            accountViewModel.setup2FA()
                        },
                        onDisable2FA = {
                            accountViewModel.disable2FA()
                        },
                        onDeleteAccount = {
                            accountViewModel.deleteAccount()
                            sessionViewModel.logout()
                            navController.navigate(NavRoute.LOGIN.path)
                        },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }
            }
        }

        is AccountScreenState.ChangedPassword -> {
            val recoveryPassword = accountViewModel.recoveryPassword.collectAsState()
            InfoDialog(
                title = "Password changed successfully",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    utilityViewModel.copyToClipboard(recoveryPassword.value)
                    sessionViewModel.logout()
                    accountViewModel.clearData()
                    navController.navigate(NavRoute.LOGIN.path)
                }
            )
        }

        is AccountScreenState.Setup2FA -> {
            val secretKey = accountViewModel.secretKey.collectAsState()
            InfoDialog(
                title = "2FA Setup",
                message = "Your secret key is: \"$secretKey\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    utilityViewModel.copyToClipboard(secretKey.value)
                    accountViewModel.resetState()
                }
            )
        }

        is AccountScreenState.Error -> {
            val errorMessage = (screenState as AccountScreenState.Error).info.message
            ErrorDialog(
                onConfirm = { accountViewModel.resetState() },
                title = "Account Error",
                message = errorMessage
            )
        }
    }
}