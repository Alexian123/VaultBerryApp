package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.AddEntryDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.TopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.VaultEntryItem
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.enums.EntryModification
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.state.VaultState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.MotionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun VaultScreen(
    vaultViewModel: VaultViewModel,
    motionViewModel: MotionViewModel,
    navController: NavController
) {
    val vaultKey = navController
        .previousBackStackEntry?.savedStateHandle?.get<DecryptedKey>("vaultKey")

    val vaultState by vaultViewModel.vaultState.collectAsState()
    val decryptedEntries by vaultViewModel.filteredEntries.collectAsState()
    val motionDetected by motionViewModel.motionDetected.collectAsState()

    var showAddEntryDialog by remember { mutableStateOf(false) }
    var entryModification by remember { mutableStateOf(EntryModification.NONE) }
    var modifiedEntry by remember { mutableStateOf(null as DecryptedVaultEntry?) }

    when (vaultState) {
        is VaultState.Loading -> {
            LoadingScreen()
        }

        is VaultState.Locked -> {
            UnlockVaultScreen(onUnlock = { vaultViewModel.getEntries(vaultKey) })
        }

        is VaultState.Unlocked -> {
            DisposableEffect(vaultState) {
                motionViewModel.registerSensorListener()
                onDispose {
                    motionViewModel.unregisterSensorListener()
                }
            }

            if (motionDetected) {
                motionViewModel.resetMotionDetected()
                vaultViewModel.logout()
                navController.navigate("login")
            }

            Scaffold(
                topBar = { TopBar(
                    onSearch = { vaultViewModel.searchEntriesByTitle(it) },
                    onLogout = {
                        vaultViewModel.logout()
                        navController.navigate("login")
                    },
                    onAccountClick = {
                        navController.navigate("account")
                    }
                ) },
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
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    LazyColumn {
                        items(decryptedEntries) { decryptedEntry ->
                            VaultEntryItem(
                                decryptedEntry = decryptedEntry,
                                onEntryModification = { event, entry ->
                                    entryModification = event
                                    modifiedEntry = entry
                                },
                                inputValidator = vaultViewModel.inputValidator
                            )
                        }
                    }

                    if (showAddEntryDialog) {
                        AddEntryDialog(
                            formTitle = stringResource(R.string.add_entry_form_title),
                            onDismissRequest = { showAddEntryDialog = false },
                            onSubmit = {
                                vaultViewModel.addEntry(it)
                                showAddEntryDialog = false
                            },
                            inputValidator = vaultViewModel.inputValidator
                        )
                    }

                    when (entryModification) {
                        EntryModification.DELETE -> {
                            ConfirmActionDialog(
                                title = stringResource(R.string.delete_entry_dialog_title),
                                message = stringResource(R.string.delete_entry_dialog_message),
                                confirmButtonText = stringResource(R.string.delete_entry_action_content_description),
                                onDismissRequest = { entryModification = EntryModification.NONE },
                                onSubmit = {
                                    if (it && modifiedEntry != null) { // delete entry
                                        vaultViewModel.deleteEntry(modifiedEntry!!)
                                        entryModification = EntryModification.NONE
                                        modifiedEntry = null
                                    }
                                }
                            )
                        }

                        EntryModification.UPDATE -> {
                            ConfirmActionDialog(
                                title = stringResource(R.string.update_entry_dialog_title),
                                message = stringResource(R.string.update_entry_dialog_message),
                                confirmButtonText = stringResource(R.string.update_entry_action_content_description),
                                onDismissRequest = { entryModification = EntryModification.NONE },
                                onSubmit = {
                                    if (it && modifiedEntry != null) { // update entry
                                        vaultViewModel.updateEntry(modifiedEntry!!)
                                        entryModification = EntryModification.NONE
                                        modifiedEntry = null
                                    }
                                }
                            )
                        }

                        EntryModification.NONE -> {}
                    }
                }
            }
        }

        is VaultState.Error ->  {
            val errorMessage = (vaultState as VaultState.Error).message
            ErrorDialog(
                onConfirm = { vaultViewModel.resetState() },
                title = "Vault Error",
                message = errorMessage
            )
        }
    }
}