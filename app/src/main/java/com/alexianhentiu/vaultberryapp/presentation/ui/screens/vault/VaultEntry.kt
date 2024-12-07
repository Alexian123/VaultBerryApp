package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.auth.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun VaultEntry(decryptedEntry: DecryptedVaultEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column (verticalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Title: " + decryptedEntry.title
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "URL: " + (decryptedEntry.url ?: "")
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Username: " + decryptedEntry.username
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Password: " + decryptedEntry.password
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Notes: " + (decryptedEntry.notes ?: "")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VaultEntryPreview() {
    VaultEntry(
        DecryptedVaultEntry(
            0,
            "Account 1",
            "https://example.com",
            "john",
            "password123",
            "My first vault entry"
        )
    )
}