package com.alexianhentiu.vaultberryapp.presentation.ui.components.items

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
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
import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultEntry
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ConfirmActionDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.enums.EntryModification

@Composable
fun VaultEntryItem(
    decryptedEntry: DecryptedVaultEntry,
    onEntryModification: (EntryModification, DecryptedVaultEntry) -> Unit,
    inputValidator: InputValidator
) {
    var title by remember { mutableStateOf(decryptedEntry.title) }
    var url by remember { mutableStateOf(decryptedEntry.url) }
    var username by remember { mutableStateOf(decryptedEntry.username) }
    var password by remember { mutableStateOf(decryptedEntry.password) }
    var notes by remember { mutableStateOf(decryptedEntry.notes) }

    var isExpanded by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    var isTitleValid by remember { mutableStateOf(inputValidator.validateEntryTitle(title)) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var unsavedChanges by remember { mutableStateOf(false) }

    if (showCancelDialog) {
        ConfirmActionDialog(
            title = stringResource(R.string.cancel_entry_dialog_title),
            message = stringResource(R.string.cancel_entry_dialog_message),
            confirmButtonText = stringResource(R.string.cancel_entry_action_content_description),
            onDismissRequest = { showCancelDialog = false },
            onSubmit = {
                if (it) { // restore initial values
                    title = decryptedEntry.title
                    url = decryptedEntry.url
                    username = decryptedEntry.username
                    password = decryptedEntry.password
                    notes = decryptedEntry.notes
                    isExpanded = false
                    editMode = false
                    unsavedChanges = false
                    showCancelDialog = false
                }
            }
        )
    }

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
                AnimatedVisibility(
                    visible = !isExpanded,
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .align(Alignment.CenterStart)

                        ) {
                            Icon(
                                imageVector = Icons.Filled.Key,
                                contentDescription = stringResource(R.string
                                    .entry_content_description),
                                modifier = Modifier.padding(10.dp)
                            )
                            Text(
                                text = decryptedEntry.title,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            )
                        }

                        IconButton(
                            onClick = { isExpanded = true },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                stringResource(R.string.toggle_entry_expansion_action_content_description)
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            ValidatedTextField(
                                readOnly = !editMode,
                                modifier = Modifier.fillMaxWidth(),
                                label = "Title",
                                initialText = title,
                                onInputChange = { newTitle, valid ->
                                    title = newTitle
                                    isTitleValid = valid
                                    unsavedChanges = true
                                },
                                isValid = inputValidator::validateEntryTitle
                            )
                            IconButton(
                                onClick = { isExpanded = false },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    Icons.Filled.ArrowDropUp,
                                    stringResource(R.string.toggle_entry_expansion_action_content_description)
                                )
                            }
                        }
                        ValidatedTextField(
                            readOnly = !editMode,
                            modifier = Modifier.fillMaxWidth(),
                            label = "URL",
                            initialText = url,
                            onInputChange = { newUrl, _ ->
                                url = newUrl
                                unsavedChanges = true
                            },
                            showCopyToClipboardButton = true
                        )
                        ValidatedTextField(
                            readOnly = !editMode,
                            modifier = Modifier.fillMaxWidth(),
                            label = "Username",
                            initialText = username,
                            onInputChange = { newUsername, _ ->
                                username = newUsername
                                unsavedChanges = true
                            },
                            showCopyToClipboardButton = true
                        )
                        PasswordField(
                            readOnly = !editMode,
                            onPasswordChange = { newPassword, _ ->
                                password = newPassword
                                unsavedChanges = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            initialText = password,
                            showCopyToClipboardButton = true
                        )
                        ValidatedTextField(
                            readOnly = !editMode,
                            modifier = Modifier.fillMaxWidth(),
                            label = "Notes",
                            initialText = notes,
                            onInputChange = { newNotes, _ ->
                                notes = newNotes
                                unsavedChanges = true
                            },
                            showCopyToClipboardButton = true
                        )
                        if (!editMode) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                IconButton(
                                    onClick = { editMode = true },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        stringResource(R.string.entry_edit_mode_action_content_description)
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(0.1f),
                                    onClick = {
                                        onEntryModification(EntryModification.DELETE, decryptedEntry)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.DeleteOutline,
                                        stringResource(R.string.delete_entry_action_content_description)
                                    )
                                }
                                Spacer(Modifier.weight(0.66f))
                                IconButton(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(0.1f),
                                    onClick = {
                                        if (!unsavedChanges) {
                                            editMode = false
                                        }
                                        showCancelDialog = unsavedChanges
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        stringResource(R.string.cancel_entry_action_content_description)
                                    )
                                }
                                IconButton(
                                    enabled = isTitleValid,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(0.1f),
                                    onClick = {

                                        editMode = false
                                        if (unsavedChanges) {
                                            unsavedChanges = false
                                            val updatedEntry = decryptedEntry.copy(
                                                title = title,
                                                url = url,
                                                username = username,
                                                password = password,
                                                notes = notes
                                            )
                                            onEntryModification(EntryModification.UPDATE, updatedEntry)
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Check,
                                        stringResource(R.string.update_entry_action_content_description)
                                    )
                                }
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
        DecryptedVaultEntry(
            0,
            "Account 1",
            "https://example.com",
            "john",
            "password123",
            "Lorem ipsum dolor"
        ),
        onEntryModification = { _, _ -> },
        inputValidator = InputValidator()
    )
}