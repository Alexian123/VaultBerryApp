package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.animations.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.state.VaultState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun VaultScreen(
    viewModel: VaultViewModel,
    navController: NavController
) {
    val vaultKey = navController
        .previousBackStackEntry?.savedStateHandle?.get<DecryptedVaultKey>("vaultKey") ?: run {
            Log.e("VaultScreen", "decryptedVaultKey is null")
            navController.popBackStack() // Navigate back to the previous screen
            return
        }

    val vaultState by viewModel.vaultState.collectAsState()
    val decryptedEntries by viewModel.decryptedEntries.collectAsState()

    var showAddEntryDialog by remember { mutableStateOf(false) }

    var modifyEntryEvent by remember { mutableStateOf(ModifyEntryEvent.NO_EVENT) }
    var modfiedEntry by remember { mutableStateOf(null as DecryptedVaultEntry?) }

    when (vaultState) {
        is VaultState.Locked -> {
            viewModel.getEntries(vaultKey)
        }
        is VaultState.Loading -> {
            LoadingScreen()
        }
        is VaultState.Unlocked -> {
            LazyColumn {
                items(decryptedEntries) { decryptedEntry ->
                    VaultEntryItem(
                        decryptedEntry = decryptedEntry,
                        onModifyEntryEvent = { event, entry ->
                            modifyEntryEvent = event
                            modfiedEntry = entry
                        },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onClick = { showAddEntryDialog = true }
                ) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(R.string.add_entry_action_content_description)
                    )
                }
            }

            if (showAddEntryDialog) {
                AddEntryDialog(
                    formTitle = stringResource(R.string.add_entry_form_title),
                    onDismissRequest = { showAddEntryDialog = false },
                    onSubmit = {
                        viewModel.addEntry(vaultKey, it)
                        showAddEntryDialog = false
                    }
                )
            }

            when (modifyEntryEvent) {
                ModifyEntryEvent.DELETE -> {
                    ConfirmActionDialog(
                        title = stringResource(R.string.delete_entry_dialog_title),
                        message = stringResource(R.string.delete_entry_dialog_message),
                        confirmButtonText = stringResource(R.string.delete_entry_action_content_description),
                        onDismissRequest = { modifyEntryEvent = ModifyEntryEvent.NO_EVENT },
                        onSubmit = {
                            if (it && modfiedEntry != null) { // delete entry
                                viewModel.deleteEntry(modfiedEntry!!)
                                modifyEntryEvent = ModifyEntryEvent.NO_EVENT
                                modfiedEntry = null
                            }
                        }
                    )
                }

                ModifyEntryEvent.UPDATE -> {
                    ConfirmActionDialog(
                        title = stringResource(R.string.update_entry_dialog_title),
                        message = stringResource(R.string.update_entry_dialog_message),
                        confirmButtonText = stringResource(R.string.update_entry_action_content_description),
                        onDismissRequest = { modifyEntryEvent = ModifyEntryEvent.NO_EVENT },
                        onSubmit = {
                            if (it && modfiedEntry != null) { // update entry
                                viewModel.updateEntry(vaultKey, modfiedEntry!!)
                                modifyEntryEvent = ModifyEntryEvent.NO_EVENT
                                modfiedEntry = null
                            }
                        }
                    )
                }

                ModifyEntryEvent.NO_EVENT -> {}
            }
        }
        is VaultState.Error ->  {
            val errorMessage = (vaultState as VaultState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}