package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry

@Composable
fun VaultEntryDialog(
    formTitle: String,
    initialEntry: DecryptedVaultEntry? = null,
    onDismissRequest: () -> Unit,
    onSubmit: (DecryptedVaultEntry) -> Unit
) {
    var entryTitle by remember { mutableStateOf(initialEntry?.title ?: "") }
    var entryUrl by remember { mutableStateOf(initialEntry?.url ?: "") }
    var entryUsername by remember { mutableStateOf(initialEntry?.username ?: "") }
    var entryPassword by remember { mutableStateOf(initialEntry?.password ?: "") }
    var entryNotes by remember { mutableStateOf(initialEntry?.notes ?: "") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f) // Adjust height as needed
                    .padding(16.dp)
            ) {
                Box( // Wrap Column in Box for centering
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth() // Make Box fill Card width
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = formTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )

                        OutlinedTextField(
                            value = entryTitle,
                            onValueChange = { entryTitle = it },
                            label = { Text("Title") },
                        )
                        OutlinedTextField(
                            value = entryUrl,
                            onValueChange = { entryUrl = it },
                            label = { Text("URL") }
                        )
                        OutlinedTextField(
                            value = entryUsername,
                            onValueChange = { entryUsername = it },
                            label = { Text("Username") }
                        )
                        OutlinedTextField(
                            value = entryPassword,
                            onValueChange = { entryPassword = it },
                            label = { Text("Password") }
                        )
                        OutlinedTextField(
                            value = entryNotes,
                            onValueChange = { entryNotes = it },
                            label = { Text("Notes") }
                        )

                        Row {
                            Button(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(16.dp),
                                onClick = {
                                    val entry: DecryptedVaultEntry
                                    if (initialEntry != null) {
                                        entry = initialEntry.copy(
                                            title = entryTitle,
                                            url = entryUrl,
                                            username = entryUsername,
                                            password = entryPassword,
                                            notes = entryNotes
                                        )
                                    } else {
                                        entry = DecryptedVaultEntry(
                                            timestamp = System.currentTimeMillis(),
                                            title = entryTitle,
                                            url = entryUrl,
                                            username = entryUsername,
                                            password = entryPassword,
                                            notes = entryNotes
                                        )
                                    }
                                    onSubmit(entry)
                                }
                            ) {
                                Text("Confirm")
                            }
                            Button(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(16.dp),
                                onClick = onDismissRequest
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VaultEntryFormPreview() {
    VaultEntryDialog(
        formTitle = "Add new entry",
        onDismissRequest = {},
        onSubmit = {}
    )
}