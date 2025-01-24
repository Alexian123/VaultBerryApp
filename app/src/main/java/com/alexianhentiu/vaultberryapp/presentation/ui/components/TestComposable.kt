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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopNavigationBarWithSearch() {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    TopAppBar(
        title = {
            if (isSearching) {
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (searchText.text.isEmpty()) {
                            Text("Search...", style = MaterialTheme.typography.bodyMedium)
                        }
                        innerTextField()
                    }
                )
            } else {
                Text("My App")
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle menu click */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            if (isSearching) {
                IconButton(onClick = {
                    isSearching = false
                    searchText = TextFieldValue("")
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            } else {
                IconButton(onClick = { isSearching = true }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun PreviewTopNavigationBarWithSearch() {
    MaterialTheme {
        TopNavigationBarWithSearch()
    }
}