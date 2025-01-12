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
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.animations.LoadingScreen
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

    var entryToModify by remember { mutableStateOf(null as DecryptedVaultEntry?) }
    var showModifyEntryDialog by remember { mutableStateOf(false) }

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
                    VaultEntryItem(viewModel, decryptedEntry) {
                        showModifyEntryDialog = it
                        entryToModify = decryptedEntry
                    }
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
                        stringResource(R.string.add_entry_action_icon_content_description)
                    )
                }
            }
            if (showAddEntryDialog) {
                VaultEntryDialog(
                    formTitle = stringResource(R.string.add_entry_form_title),
                    onDismissRequest = { showAddEntryDialog = false },
                    onSubmit = {
                        viewModel.addEntry(vaultKey, it)
                        showAddEntryDialog = false
                    }
                )
            } else if (showModifyEntryDialog) {
                VaultEntryDialog(
                    formTitle = stringResource(R.string.modify_entry_form_title),
                    initialEntry = entryToModify,
                    onDismissRequest = { showModifyEntryDialog = false },
                    onSubmit = {
                        viewModel.modifyEntry(vaultKey, it)
                        showModifyEntryDialog = false
                    }
                )
            }
        }
        is VaultState.Error ->  {
            val errorMessage = (vaultState as VaultState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}