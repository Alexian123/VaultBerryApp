package com.alexianhentiu.vaultberryapp.presentation.ui.screens.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.AccountForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.account.AccountViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel

@Composable
fun AccountScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    utilityViewModel: UtilityViewModel,
    settingsViewModel: SettingsViewModel,
    accountViewModel: AccountViewModel = hiltViewModel(),
) {
    val screenState by accountViewModel.accountScreenState.collectAsState()
    val secretKey by accountViewModel.secretKeyEvent.collectAsState(initial = "")
    val recoveryPassword by accountViewModel.recoveryPasswordEvent.collectAsState(initial = "")

    val isDebugMode by settingsViewModel.debugMode.collectAsState()

    var show2FAInfoDialog by remember { mutableStateOf(false) }
    var showRecoveryPasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(secretKey) {
        if (secretKey.isNotEmpty()) {
            show2FAInfoDialog = true
        }
    }

    LaunchedEffect(recoveryPassword) {
        if (recoveryPassword.isNotEmpty()) {
            showRecoveryPasswordDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = stringResource(R.string.account_screen_title)
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
                            accountViewModel.changeAccountInfo(
                                email,
                                firstName,
                                lastName,
                                isDebugMode
                            )
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
                            accountViewModel.setLoadingState()
                            accountViewModel.clearData()
                            sessionViewModel.resetState()
                            navController.navigate(NavRoute.LOGIN.path)
                        },
                        validator = {
                            utilityViewModel.getValidatorFunction(it, isDebugMode)
                        },
                        evaluatePasswordStrength = utilityViewModel::evalPasswordStrength
                    )
                }

                is AccountScreenState.ChangedEmail -> {
                    InfoDialog(
                        title = stringResource(R.string.account_screen_changed_email_title),
                        message = stringResource(R.string.account_screen_changed_email_message),
                        onDismissRequest = {
                            accountViewModel.setLoadingState()
                            accountViewModel.clearData()
                            sessionViewModel.logout()
                            navController.navigate(NavRoute.LOGIN.path)
                        }
                    )
                }

                is AccountScreenState.ChangedPassword -> {
                    if (showRecoveryPasswordDialog) {
                        InfoDialog(
                            title = stringResource(R.string.account_screen_changed_password_title),
                            message = stringResource(R.string.recovery_password_message_p1) +
                                    "\n$recoveryPassword" +
                                    stringResource(R.string.recovery_password_message_p2) +
                                    stringResource(R.string.recovery_password_message_p3),
                            onDismissRequest = {
                                utilityViewModel.copyToClipboard(recoveryPassword)
                                accountViewModel.setLoadingState()
                                accountViewModel.clearData()
                                sessionViewModel.logout()
                                navController.navigate(NavRoute.LOGIN.path)
                            }
                        )
                    }
                }

                is AccountScreenState.Setup2FA -> {
                    if (show2FAInfoDialog) {
                        InfoDialog(
                            title = stringResource(R.string.account_screen_setup_2fa_title),
                            message = stringResource(R.string.account_screen_setup_2fa_message_p1) +
                                    "\n$secretKey" +
                                    stringResource(R.string.account_screen_setup_2fa_message_p2),
                            onDismissRequest = {
                                utilityViewModel.copyToClipboard(secretKey)
                                accountViewModel.resetState()
                            }
                        )
                    }
                }

                is AccountScreenState.Error -> {
                    val errorInfo = (screenState as AccountScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { accountViewModel.resetState() },
                        errorInfo = errorInfo,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail,
                                appName = utilityViewModel.getAppInfo(AppInfo.APP_NAME),
                                appVersionName = utilityViewModel.getAppInfo(AppInfo.VERSION_NAME),
                            )
                        }
                    )
                }
            }
        }
    }
}