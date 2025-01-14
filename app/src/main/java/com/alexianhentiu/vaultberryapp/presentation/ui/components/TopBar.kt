package com.alexianhentiu.vaultberryapp.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.alexianhentiu.vaultberryapp.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onSearch: (String) -> Unit,
    onLogout: () -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (isSearching) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onSearch(it.text)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (searchText.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search_bar_text),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        innerTextField()
                    }
                )
            } else {
                Text(stringResource(R.string.app_name))
            }
        },
        navigationIcon = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    Icons.Filled.Menu,
                    stringResource(R.string.menu_content_description)
                )
            }
            MainMenu(
                onDismissRequest = { showMenu = false },
                expanded = showMenu,
                onLogout = onLogout
            )
        },
        actions = {
            if (isSearching) {
                IconButton(onClick = {
                    isSearching = false
                    searchText = TextFieldValue("")
                    onSearch("")
                }) {
                    Icon(
                        Icons.Filled.Close,
                        stringResource(R.string.close_content_description)
                    )
                }
            } else {
                IconButton(onClick = { isSearching = true }) {
                    Icon(
                        Icons.Filled.Search,
                        stringResource(R.string.search_content_description)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    TopBar(
        onSearch = { },
        onLogout = { }
    )
}