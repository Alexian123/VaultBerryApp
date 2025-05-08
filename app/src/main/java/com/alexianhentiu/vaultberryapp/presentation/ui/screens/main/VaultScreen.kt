package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.VaultEntryDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.VaultTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.VaultEntryItem
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.UnlockVaultScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.VaultViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.states.VaultState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VaultScreen(
    vaultViewModel: VaultViewModel,
    navManager: NavigationManager
) {
    val vaultKey = navManager.retrieveVaultKey()

    val vaultState by vaultViewModel.vaultState.collectAsState()
    val previews by vaultViewModel.filteredPreviews.collectAsState()
    val expandedEntriesMap by vaultViewModel.expandedEntriesMap.collectAsState()
    val isRefreshing by vaultViewModel.isRefreshing.collectAsState()

    var entryToModifyId by remember { mutableLongStateOf(-1L) }

    var showAddEntryDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteEntryDialog by remember { mutableStateOf(false) }
    var showEditEntryDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showLogoutDialog = true
    }

    when (vaultState) {
        is VaultState.Loading -> {
            LoadingScreen()
        }

        is VaultState.Locked -> {
            UnlockVaultScreen(onUnlock = { vaultViewModel.fetchPreviews(vaultKey) })
        }

        is VaultState.Unlocked -> {
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = { vaultViewModel.refreshVaultEntries() }
            )

            Scaffold(
                topBar = {
                    VaultTopBar(
                        onSearch = { vaultViewModel.searchPreviewsByTitle(it) },
                        onLogout = {
                            vaultViewModel.logout()
                            navManager.navigate(NavRoute.LOGIN)
                        },
                        onAccountClick = {
                            navManager.navigateWithVaultKey(NavRoute.ACCOUNT, vaultKey)
                        },
                        onSettingsClick = {
                            navManager.navigateWithVaultKey(NavRoute.SETTINGS, vaultKey)
                        }
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
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .pullRefresh(pullRefreshState)
                ) {
                    LazyColumn {
                        items(previews, key = { it.id }) { entryPreview ->
                            VaultEntryItem(
                                preview = entryPreview,
                                decryptedEntry = expandedEntriesMap[entryPreview.id],
                                onItemClick = { id ->
                                    if (expandedEntriesMap[id] != null) {
                                        vaultViewModel.clearEntryDetails(id)
                                    } else {
                                        vaultViewModel.fetchEntryDetails(id)
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
                                passwordEvaluator = vaultViewModel.passwordEvaluator
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
                                        vaultViewModel.updateEntry(entryToModifyId, it)
                                        entryToModifyId = -1L
                                        showEditEntryDialog = false
                                    }
                                },
                                inputValidator = vaultViewModel.inputValidator,
                                passwordEvaluator = vaultViewModel.passwordEvaluator
                            )
                        }
                        showLogoutDialog -> {
                            ConfirmActionDialog(
                                title = "Log Out",
                                message = "Are you sure you want to log out?",
                                onDismissRequest = { showLogoutDialog = false },
                                onSubmit = {
                                    if (it) {
                                        vaultViewModel.logout()
                                        navManager.navigate(NavRoute.LOGIN)
                                    }
                                }
                            )
                        }
                        showAddEntryDialog -> {
                            VaultEntryDialog(
                                formTitle = stringResource(R.string.add_entry_form_title),
                                onDismissRequest = { showAddEntryDialog = false },
                                onSubmit = {
                                    vaultViewModel.addEntry(it)
                                    showAddEntryDialog = false
                                },
                                inputValidator = vaultViewModel.inputValidator,
                                passwordEvaluator = vaultViewModel.passwordEvaluator
                            )
                        }
                    }
                }
            }
        }

        is VaultState.Error ->  {
            val errorMessage = (vaultState as VaultState.Error).info.message
            ErrorDialog(
                onConfirm = { vaultViewModel.resetState() },
                title = "Vault Error",
                message = errorMessage
            )
        }
    }
}