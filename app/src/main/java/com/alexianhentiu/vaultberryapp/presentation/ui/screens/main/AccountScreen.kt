package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexianhentiu.vaultberryapp.presentation.activity.MainActivity
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.AccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AccountState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel

@Composable
fun AccountScreen(
    navManager: NavigationManager
) {
    val activity = LocalActivity.current as MainActivity
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val accountViewModel: AccountViewModel = hiltViewModel()

    val vaultKey = navManager.retrieveVaultKey()

    val accountState by accountViewModel.accountState.collectAsState()

    val clipboardManager = LocalClipboardManager.current

    BackHandler(enabled = true) {
        // send key to vault screen
        navManager.navigateWithVaultKey(NavRoute.VAULT, vaultKey)
    }

    when (accountState) {
        is AccountState.Init -> {
            accountViewModel.getAccountInfo()
        }

        is AccountState.Loading -> {
            LoadingScreen()
        }

        is AccountState.Idle -> {
            val state = accountState as AccountState.Idle
            val account = state.accountInfo
            val is2FAEnabled = state.is2FAEnabled

            Scaffold(
                topBar = {
                    TopBarWithBackButton(
                        onBackClick = {
                            // send key to vault screen
                            navManager.navigateWithVaultKey(NavRoute.VAULT, vaultKey)
                        },
                        title = "Account"
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    AccountForm(
                        accountInfo = account,
                        is2FAEnabled = is2FAEnabled,
                        onSaveInfo = { email, firstName, lastName ->
                            accountViewModel.changeAccountInfo(email, firstName, lastName)
                        },
                        onChangePassword = { newPassword, reEncrypt ->
                            if (vaultKey != null) {
                                accountViewModel.changePassword(vaultKey, newPassword, reEncrypt)
                            }
                        },
                        onEnable2FA = {
                            accountViewModel.setup2FA()
                        },
                        onDisable2FA = {
                            accountViewModel.disable2FA()
                        },
                        onDeleteAccount = {
                            accountViewModel.deleteAccount()
                        },
                        inputValidator = accountViewModel.inputValidator,
                        passwordEvaluator = accountViewModel.passwordEvaluator
                    )
                }
            }
        }

        is AccountState.LoggedOut -> {
            navManager.navigate(NavRoute.LOGIN)
        }

        is AccountState.ChangedPassword -> {
            val recoveryPassword = (accountState as AccountState.ChangedPassword).recoveryPassword
            InfoDialog(
                title = "Password changed successfully",
                message = "Your new recovery password is: \"$recoveryPassword\". " +
                        "It will be copied into the clipboard upon confirmation.",
                onDismissRequest = {
                    clipboardManager.setText(AnnotatedString(recoveryPassword))
                    accountViewModel.logout()
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
                    accountViewModel.resetState()
                }
            )
        }

        is AccountState.Error -> {
            val errorMessage = (accountState as AccountState.Error).info.message
            ErrorDialog(
                onConfirm = { accountViewModel.resetState() },
                title = "Account Error",
                message = errorMessage
            )
        }
    }
}