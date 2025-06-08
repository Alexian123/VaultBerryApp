package com.alexianhentiu.vaultberryapp.presentation.ui.screens.apiConfig

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.SuccessAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.ApiConfigForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.TopBarWithBackButton

@Composable
fun ApiConfigScreen(
    navController: NavHostController,
    utilityViewModel: UtilityViewModel,
    apiConfigViewModel: ApiConfigViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val screenState by apiConfigViewModel.screenState.collectAsState()
    val pingState by apiConfigViewModel.pingState.collectAsState()

    val url by apiConfigViewModel.url.collectAsState()
    val certificate by apiConfigViewModel.certificate.collectAsState()

    // Launcher for the file selection activity result
    val selectPemFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                apiConfigViewModel.saveApiCertificate(uri)
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                navController = navController,
                title = stringResource(R.string.api_config_screen_title)
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {

            when (screenState) {
                is ApiConfigScreenState.Idle -> {
                    apiConfigViewModel.loadExistingData()
                }

                is ApiConfigScreenState.Loading -> {
                    LoadingAnimationDialog()
                }

                is ApiConfigScreenState.Ready -> {
                    var isPinging = false
                    when (pingState) {
                        is PingState.Success -> {
                            val host = (pingState as PingState.Success).host
                            val port = (pingState as PingState.Success).port
                            val address = "$host:$port"
                            InfoDialog(
                                onDismissRequest = { apiConfigViewModel.resetPingState() },
                                title = stringResource(R.string.success_title),
                                message = "Host $address is reachable"
                            )
                        }

                        is PingState.Failure -> {
                            val message = (pingState as PingState.Failure).message
                            InfoDialog(
                                onDismissRequest = { apiConfigViewModel.resetPingState() },
                                title = stringResource(R.string.failure_title),
                                message = message
                            )
                        }

                        is PingState.Loading -> isPinging = true
                        is PingState.Idle -> isPinging = false
                    }

                    ApiConfigForm(
                        apiUrl = url,
                        apiCertificate = certificate,
                        isPinging = isPinging,
                        onSaveUrlClicked = {
                            apiConfigViewModel.saveApiUrl(it)
                        },
                        onTestConnectionClicked = { ip, port ->
                            apiConfigViewModel.pingApi(ip, port)
                        },
                        onImportCertClicked = {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "*/*" // Start with a broad type
                                val mimeTypes = arrayOf(
                                    "application/x-x509-ca-cert",
                                    "application/pkix-cert",
                                    "application/x-pem-file",
                                    "text/plain" // Fallback for general text files
                                )
                                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                            }
                            selectPemFileLauncher.launch(intent) // Launch the file manager
                        },
                        onClearCertClicked = {
                            apiConfigViewModel.clearApiCertificate()
                        },
                        onExitClicked = {
                            val activity = context as? Activity
                            activity?.finishAffinity()
                        }
                    )
                }

                is ApiConfigScreenState.Success -> {
                    var shownAnimation by remember { mutableStateOf(false) }
                    if (!shownAnimation) {
                        SuccessAnimationDialog(
                            displayDurationMillis = 1000,
                            onTimeout = {
                                shownAnimation = true
                                apiConfigViewModel.setReadyState()
                            }
                        )
                    }
                }

                is ApiConfigScreenState.Error -> {
                    val errorInfo = (screenState as ApiConfigScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { apiConfigViewModel.resetState() },
                        errorInfo = errorInfo,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail,
                                appName = utilityViewModel.getAppInfo(AppInfo.APP_NAME),
                                appVersionName = utilityViewModel.getAppInfo(
                                    AppInfo.VERSION_NAME
                                ),
                            )
                        }
                    )
                }
            }
        }
    }
}