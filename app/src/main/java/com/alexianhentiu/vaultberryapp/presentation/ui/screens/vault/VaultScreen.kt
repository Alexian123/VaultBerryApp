package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.presentation.ui.state.VaultState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun VaultScreen(
    viewmodel: VaultViewModel,
    navController: NavController
) {
    val vaultKey = navController
        .previousBackStackEntry?.savedStateHandle?.get<DecryptedVaultKey>("vaultKey") ?: run {
            Log.e("VaultScreen", "decryptedVaultKey is null")
            navController.popBackStack() // Navigate back to the previous screen
            return
        }

    val vaultState by viewmodel.vaultState.collectAsState()
    val decryptedEntries by viewmodel.decryptedEntries.collectAsState()

    when (vaultState) {
        is VaultState.Locked -> {
            viewmodel.getEntries(vaultKey)
        }
        is VaultState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is VaultState.Unlocked -> {
            LazyColumn {
                items(decryptedEntries) { decryptedEntry ->
                    VaultEntry(decryptedEntry)
                }
            }
        }
        is VaultState.Error ->  {
            val errorMessage = (vaultState as VaultState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}