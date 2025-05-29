package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.AccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.helper.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.AccountScreenState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel

@Composable
fun AccountScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    utilityViewModel: UtilityViewModel,
    accountViewModel: AccountViewModel = hiltViewModel(),
) {
    val inputValidator by utilityViewModel.inputValidator.collectAsState()
    val screenState by accountViewModel.accountScreenState.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = "Account"
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

            when (screenState) {
                is AccountScreenState.Init -> {
                    accountViewModel.getAccountInfo()
                }

                is AccountScreenState.Loading -> {
                    LoadingAnimationDialog()
                }

                is AccountScreenState.Idle -> {
                    val account = accountViewModel.accountInfo.collectAsState()
                    val is2FAEnabled = accountViewModel.is2FAEnabled.collectAsState()
                    val vaultKey = sessionViewModel.decryptedKey.collectAsState()

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

                is AccountScreenState.ChangedPassword -> {
                    val recoveryPassword = accountViewModel.recoveryPassword.collectAsState()
                    InfoDialog(
                        title = "Password changed successfully",
                        message = "Your new recovery password is:\n${recoveryPassword.value}\n" +
                                "It will be copied into the clipboard upon confirmation.",
                        onDismissRequest = {
                            utilityViewModel.copyToClipboard(recoveryPassword.value)
                            accountViewModel.setLoadingState()
                            accountViewModel.clearData()
                            sessionViewModel.logout()
                            navController.navigate(NavRoute.LOGIN.path)
                        }
                    )
                }

                is AccountScreenState.Setup2FA -> {
                    val secretKey = accountViewModel.secretKey.collectAsState()
                    InfoDialog(
                        title = "2FA Setup",
                        message = "Your secret key is:\n${secretKey.value}\n" +
                                "It will be copied into the clipboard upon confirmation.",
                        onDismissRequest = {
                            utilityViewModel.copyToClipboard(secretKey.value)
                            accountViewModel.resetState()
                        }
                    )
                }

                is AccountScreenState.Error -> {
                    val errorInfo = (screenState as AccountScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { accountViewModel.resetState() },
                        title = "${errorInfo.type.name} ERROR",
                        source = errorInfo.source,
                        message = errorInfo.message,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail
                            )
                        }
                    )
                }
            }
        }
    }
}