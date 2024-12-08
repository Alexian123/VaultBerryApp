package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VaultEntryItem(
    viewModel: VaultViewModel?,
    decryptedEntry: DecryptedVaultEntry,
    onValueChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.8f)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = { onValueChange(true) }
                    )
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Title: " + decryptedEntry.title
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "URL: " + decryptedEntry.url
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
                    text = "Notes: " + decryptedEntry.notes
                )
            }
            Button(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp),
                onClick = { viewModel?.removeEntry(decryptedEntry) }
            ) {
                Icon(
                    Icons.Filled.Delete,
                    stringResource(R.string.delete_entry_action_icon_content_description)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VaultEntryPreview() {
    VaultEntryItem(
        null,
        DecryptedVaultEntry(
            0,
            "Account 1",
            "https://example.com",
            "john",
            "password123",
            "My first vault entry"
        ),
        onValueChange = {}
    )
}