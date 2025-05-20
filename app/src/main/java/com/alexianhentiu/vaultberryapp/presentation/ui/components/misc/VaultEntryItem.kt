package com.alexianhentiu.vaultberryapp.presentation.ui.components.misc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.alexianhentiu.vaultberryapp.domain.model.entity.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.model.entity.VaultEntryPreview
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun VaultEntryItem(
    preview: VaultEntryPreview,
    decryptedEntry: DecryptedVaultEntry? = null,    // null if not expanded
    onItemClick: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    evaluatePasswordStrength: (String) -> PasswordStrength = { PasswordStrength.NONE },
    onCopyClicked: (String) -> Unit
) {
    var title by remember(preview) { mutableStateOf(preview.title) }
    var url by remember(decryptedEntry) { mutableStateOf(decryptedEntry?.url ?: "") }
    var username by remember(decryptedEntry) { mutableStateOf(decryptedEntry?.username ?: "") }
    var password by remember(decryptedEntry) { mutableStateOf(decryptedEntry?.password ?: "") }
    var notes by remember(decryptedEntry) { mutableStateOf(decryptedEntry?.notes ?: "") }

    var passwordVisible by remember { mutableStateOf(false) }

    if (decryptedEntry == null) {
        // Reset values
        passwordVisible = false
        title = preview.title
        url = ""
        username = ""
        password = ""
        notes = ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                passwordVisible = false
                onItemClick(preview.id)
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(vertical = if (decryptedEntry == null) 0.dp else 8.dp)
            ) {
                AnimatedVisibility(
                    visible = decryptedEntry == null,
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Key,
                                contentDescription = stringResource(
                                    R.string.entry_content_description
                                ),
                                modifier = Modifier.padding(10.dp)
                            )
                            Text(
                                text = preview.title,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }

                        IconButton(
                            onClick = { onItemClick(preview.id) },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                stringResource(
                                    R.string.toggle_entry_expansion_action_content_description
                                )
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = decryptedEntry != null,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            ValidatedTextField(
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                label = "Title",
                                initialText = title,
                            )
                            IconButton(
                                onClick = { onItemClick(preview.id) },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    Icons.Filled.ArrowDropUp,
                                    stringResource(
                                        R.string.toggle_entry_expansion_action_content_description
                                    )
                                )
                            }
                        }
                        ValidatedTextField(
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            label = "URL",
                            initialText = url,
                            showCopyToClipboardButton = true,
                            onCopyClicked = onCopyClicked
                        )
                        ValidatedTextField(
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            label = "Username",
                            initialText = username,
                            showCopyToClipboardButton = true,
                            onCopyClicked = onCopyClicked
                        )
                        PasswordField(
                            readOnly = true,
                            onPasswordChange = { _, _ -> },
                            modifier = Modifier.fillMaxWidth(),
                            initialText = password,
                            showCopyToClipboardButton = true,
                            onCopyClicked = onCopyClicked,
                            evaluateStrength = evaluatePasswordStrength,
                            showStrengthIndicator = true,
                        )
                        ValidatedTextField(
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            label = "Notes",
                            initialText = notes,
                            showCopyToClipboardButton = true,
                            onCopyClicked = onCopyClicked
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                modifier = Modifier.weight(0.1f),
                                onClick = { onDelete(preview.id) }
                            ) {
                                Icon(
                                    Icons.Filled.DeleteOutline,
                                    stringResource(R.string.delete_entry_action_content_description)
                                )
                            }
                            Spacer(Modifier.weight(0.8f))
                            IconButton(
                                onClick = { onEdit(preview.id) },
                                modifier = Modifier.weight(0.1f),
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    stringResource(R.string.entry_edit_mode_action_content_description)
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
        preview = VaultEntryPreview(1, "Entry"),
        decryptedEntry = null,
        onItemClick = {},
        onDelete = {},
        onEdit = {},
        onCopyClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ExpandedVaultEntryPreview() {
    VaultEntryItem(
        preview = VaultEntryPreview(1, "Entry"),
        decryptedEntry = DecryptedVaultEntry(
            title = "Entry",
            url = "https://example.com",
            username = "user",
            password = "password",
            notes = "notes"
        ),
        onItemClick = {},
        onDelete = {},
        onEdit = {},
        onCopyClicked = {}
    )
}