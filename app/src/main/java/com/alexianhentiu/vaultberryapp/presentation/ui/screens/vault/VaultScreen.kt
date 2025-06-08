package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.VaultEntryDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.VaultTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.VaultEntryItem
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SettingsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VaultScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    utilityViewModel: UtilityViewModel,
    settingsViewModel: SettingsViewModel,
    vaultViewModel: VaultViewModel = hiltViewModel()
) {
    val isDebugMode by settingsViewModel.debugMode.collectAsState()

    val decryptedKey = sessionViewModel.decryptedKey.collectAsState()

    val screenState by vaultViewModel.vaultScreenState.collectAsState()
    val previews by vaultViewModel.filteredPreviews.collectAsState()
    val expandedEntriesMap by vaultViewModel.expandedEntriesMap.collectAsState()

    var entryToModifyId by remember { mutableLongStateOf(-1L) }

    var showAddEntryDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteEntryDialog by remember { mutableStateOf(false) }
    var showEditEntryDialog by remember { mutableStateOf(false) }

    val isRefreshing by vaultViewModel.isRefreshing.collectAsState()

    BackHandler(enabled = true) {
        showLogoutDialog = true
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { vaultViewModel.refreshVaultEntries() }
    )

    Scaffold(
        topBar = {
            VaultTopBar(
                onSearch = { vaultViewModel.searchPreviewsByTitle(it) },
                onAccountClick = { navController.navigate(NavRoute.ACCOUNT.path) },
                onPasswordGeneratorClick = {
                    navController.navigate(NavRoute.PASSWORD_GENERATOR.path)
                },
                onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) },
                onAboutClick = { navController.navigate(NavRoute.ABOUT.path) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddEntryDialog = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    stringResource(R.string.add_entry_action_content_description)
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .pullRefresh(pullRefreshState)
        ) {

            when (screenState) {
                is VaultScreenState.Loading -> {
                    LoadingAnimationDialog()
                }

                is VaultScreenState.Locked -> {
                    vaultViewModel.fetchPreviews()
                }

                is VaultScreenState.Unlocked -> {
                    LazyColumn {
                        items(previews, key = { it.id }) { entryPreview ->
                            VaultEntryItem(
                                preview = entryPreview,
                                decryptedEntry = expandedEntriesMap[entryPreview.id],
                                onItemClick = { id ->
                                    if (expandedEntriesMap[id] != null) {
                                        vaultViewModel.clearEntryDetails(id)
                                    } else {
                                        vaultViewModel.fetchEntryDetails(id, decryptedKey.value)
                                    }
                                },
                                onDelete = { id ->
                                    entryToModifyId = id
                                    showDeleteEntryDialog = true
                                },
                                onEdit = { id ->
                                    entryToModifyId = id
                                    showEditEntryDialog = true
                                },
                                evaluatePasswordStrength = utilityViewModel::evalPasswordStrength,
                                onCopyClicked = utilityViewModel::copyToClipboard
                            )
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    when {
                        showDeleteEntryDialog -> {
                            ConfirmActionDialog(
                                title = stringResource(R.string.delete_entry_dialog_title),
                                message = stringResource(R.string.delete_entry_dialog_message),
                                confirmButtonText = stringResource(
                                    R.string.delete_entry_action_content_description
                                ),
                                onDismissRequest = {
                                    entryToModifyId = -1L
                                    showDeleteEntryDialog = false
                                },
                                onSubmit = {
                                    if (it && entryToModifyId != -1L) {
                                        vaultViewModel.deleteEntry(entryToModifyId)
                                        entryToModifyId = -1L
                                        showDeleteEntryDialog = false
                                    }
                                }
                            )
                        }

                        showEditEntryDialog -> {
                            VaultEntryDialog(
                                formTitle = stringResource(R.string.edit_entry_dialog_title),
                                initialEntry = expandedEntriesMap[entryToModifyId],
                                onDismissRequest = {
                                    entryToModifyId = -1L
                                    showEditEntryDialog = false
                                },
                                onSubmit = {
                                    if (entryToModifyId != -1L) {
                                        vaultViewModel.updateEntry(
                                            entryToModifyId,
                                            it,
                                            decryptedKey.value
                                        )
                                        entryToModifyId = -1L
                                        showEditEntryDialog = false
                                    }
                                },
                                validator = {
                                    utilityViewModel.getValidatorFunction(it, isDebugMode)
                                },
                                evaluatePasswordStrength = utilityViewModel::evalPasswordStrength
                            )
                        }

                        showLogoutDialog -> {
                            ConfirmActionDialog(
                                title = stringResource(R.string.logout_dialog_title),
                                message = stringResource(R.string.logout_dialog_message),
                                onDismissRequest = { showLogoutDialog = false },
                                onSubmit = {
                                    if (it) {
                                        sessionViewModel.logout()
                                        vaultViewModel.clearData()
                                        navController.navigate(NavRoute.LOGIN.path)
                                    }
                                }
                            )
                        }

                        showAddEntryDialog -> {
                            VaultEntryDialog(
                                formTitle = stringResource(R.string.add_entry_form_title),
                                onDismissRequest = { showAddEntryDialog = false },
                                onSubmit = {
                                    vaultViewModel.addEntry(it, decryptedKey.value)
                                    showAddEntryDialog = false
                                },
                                validator = {
                                    utilityViewModel.getValidatorFunction(it, isDebugMode)
                                },
                                evaluatePasswordStrength = utilityViewModel::evalPasswordStrength
                            )
                        }
                    }
                }

                is VaultScreenState.Error -> {
                    val errorInfo = (screenState as VaultScreenState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { vaultViewModel.resetState() },
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