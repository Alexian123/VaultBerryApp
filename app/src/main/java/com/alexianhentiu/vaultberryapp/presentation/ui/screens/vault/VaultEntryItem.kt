package com.alexianhentiu.vaultberryapp.presentation.ui.screens.vault

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.buttons.CopyToClipboardButton
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.buttons.ToggleVisibilityButton
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.VaultViewModel

@Composable
fun VaultEntryItem(
    viewModel: VaultViewModel?,
    decryptedEntry: DecryptedVaultEntry,
    shouldModifyEntry: (Boolean) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                isExpanded = !isExpanded
                passwordVisible = false
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.8f)
            ) {
                Row {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = decryptedEntry.title
                    )
                }
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "URL: " + decryptedEntry.url
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Username: " + decryptedEntry.username
                            )
                            CopyToClipboardButton(decryptedEntry.username)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Password: " + if (passwordVisible) decryptedEntry.password else "*".repeat(
                                    decryptedEntry.password.length
                                )
                            )
                            ToggleVisibilityButton(
                                onVisibilityChanged = { passwordVisible = it },
                            )
                            CopyToClipboardButton(decryptedEntry.password)
                        }
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Notes: " + decryptedEntry.notes
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(8.dp),
                                onClick = { shouldModifyEntry(true) }
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    stringResource(R.string.modify_entry_action_content_description)
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .padding(8.dp),
                                onClick = { viewModel?.removeEntry(decryptedEntry) }
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    stringResource(R.string.delete_entry_action_content_description)
                                )
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
fun VaultEntryPreview() {
    VaultEntryItem(
        null,
        DecryptedVaultEntry(
            0,
            "Account 1",
            "https://example.com",
            "john",
            "password123",
            "Lorem ipsum dolor"
        ),
        shouldModifyEntry = {}
    )
}